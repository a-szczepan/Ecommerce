package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CartTable(tag: Tag) extends Table[Cart](tag, "cart") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")

    def userFk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def productId = column[Int]("productId")

    def productFk = foreignKey("productId", productId, product)(_.id)

    def quantity = column[Int]("quantity")

    def * = (id, providerKey, productId, quantity) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  var cart = TableQuery[CartTable]

  def create(providerKey: String, productId: Int, quantity: Int): Future[Cart] = db.run {
    (cart.map(c => (c.providerKey, c.productId, c.quantity))
      returning cart.map(_.id)
      into { case ((providerKey, productId, quantity), id) => Cart(id, providerKey, productId, quantity) }
      ) += (providerKey, productId, quantity)
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def update(id: Int, newCart: Cart): Future[Int] = {
    val cartToUpdate: Cart = newCart.copy(id)
    db.run(cart.filter(_.id === id).update(cartToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(cart.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }

  def getByUserKey(providerKey: String): Future[Seq[Cart]] = db.run {
    cart.filter(_.providerKey === providerKey).result
  }

  def getByProductId(productId: Int): Future[Seq[Cart]] = db.run {
    cart.filter(_.productId === productId).result
  }

}
