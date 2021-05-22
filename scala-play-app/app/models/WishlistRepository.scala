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

    def user_id = column[Int]("user_id")

    def user_fk = foreignKey("user_id", user_id, usr)(_.id)

    def product_id = column[Int]("product_id")

    def product_fk = foreignKey("product_id", product_id, product)(_.id)

    def * = (id, user_id, product_id) <> ((Wishlist.apply _).tupled, Wishlist.unapply)
  }

  import productRepository.ProductTable
  import userRepository.UserTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  var wishlist = TableQuery[WishlistTable]

  def create(user_id: Int, product_id: Int): Future[Wishlist] = db.run {
    (wishlist.map(w => (w.user_id, w.product_id))
      returning wishlist.map(_.id)
      into { case ((user_id, product_id), id) => Wishlist(id, user_id, product_id) }
      ) += (user_id, product_id)
  }

  def list(): Future[Seq[Wishlist]] = db.run {
    wishlist.result
  }

  def update(id: Int, new_wishlist: Wishlist): Future[Int] = {
    val wishlistToUpdate: Wishlist = new_wishlist.copy(id)
    db.run(wishlist.filter(_.id === id).update(wishlistToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(wishlist.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Wishlist]] = db.run {
    wishlist.filter(_.id === id).result.headOption
  }

  def getByUser(user_id: Int): Future[Seq[Wishlist]] = db.run {
    wishlist.filter(_.user_id === user_id).result
  }

  def getByProduct(product_id: Int): Future[Seq[Wishlist]] = db.run {
    wishlist.filter(_.product_id === product_id).result
  }
}
