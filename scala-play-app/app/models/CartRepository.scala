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

    def user_fk = foreignKey("providerKey", providerKey, usr)(_.providerKey)

    def product_id = column[Int]("product_id")

    def product_fk = foreignKey("product_id", product_id, product)(_.id)

    def quantity = column[Int]("quantity")

    def * = (id, providerKey, product_id, quantity) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  var cart = TableQuery[CartTable]

  def create(providerKey: String, product_id: Int, quantity: Int): Future[Cart] = db.run {
    (cart.map(c => (c.providerKey, c.product_id, c.quantity))
      returning cart.map(_.id)
      into { case ((providerKey, product_id, quantity), id) => Cart(id, providerKey, product_id, quantity) }
      ) += (providerKey, product_id, quantity)
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def update(id: Int, new_cart: Cart): Future[Int] = {
    val cartToUpdate: Cart = new_cart.copy(id)
    db.run(cart.filter(_.id === id).update(cartToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(cart.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }

  def getByUserKey(providerKey: String): Future[Seq[Cart]] = db.run {
    cart.filter(_.providerKey === providerKey).result
  }

  def getByProductId(product_id: Int): Future[Seq[Cart]] = db.run {
    cart.filter(_.product_id === product_id).result
  }

}
