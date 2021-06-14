package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WishlistRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class WishlistTable(tag: Tag) extends Table[Wishlist](tag, "wishlist") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")

    def userFk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def productId = column[Int]("productId")

    def productFk = foreignKey("productId", productId, product)(_.id)

    def * = (id, providerKey, productId) <> ((Wishlist.apply _).tupled, Wishlist.unapply)
  }

  import productRepository.ProductTable
  import userRepository.UserTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  var wishlist = TableQuery[WishlistTable]

  def create(providerKey: String, productId: Int): Future[Wishlist] = db.run {
    (wishlist.map(w => (w.providerKey, w.productId))
      returning wishlist.map(_.id)
      into { case ((providerKey, productId), id) => Wishlist(id, providerKey, productId) }
      ) += (providerKey, productId)
  }

  def list(): Future[Seq[Wishlist]] = db.run {
    wishlist.result
  }

  def update(id: Int, newWishlist: Wishlist): Future[Int] = {
    val wishlistToUpdate: Wishlist = newWishlist.copy(id)
    db.run(wishlist.filter(_.id === id).update(wishlistToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(wishlist.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Wishlist]] = db.run {
    wishlist.filter(_.id === id).result.headOption
  }

  def getByUserKey(providerKey: String): Future[Seq[Wishlist]] = db.run {
    wishlist.filter(_.providerKey === providerKey).result
  }

  def getByProduct(productId: Int): Future[Seq[Wishlist]] = db.run {
    wishlist.filter(_.productId === productId).result
  }
}
