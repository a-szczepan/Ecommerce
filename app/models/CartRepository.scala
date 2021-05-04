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

    def user_id = column[Int]("user_id")

    def user_fk = foreignKey("user_id", user_id, usr)(_.id)

    def product_id = column[Int]("product_id")

    def product_fk = foreignKey("product_id", product_id, product)(_.id)

    def * = (id, user_id, product_id) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val usr = TableQuery[UserTable]
  val product = TableQuery[ProductTable]
  var cart = TableQuery[CartTable]

  def create(user_id: Int, product_id: Int): Future[Cart] = db.run {
    (cart.map(c => (c.user_id, c.product_id))
      returning cart.map(_.id)
      into { case ((user_id, product_id), id) => Cart(id, user_id, product_id) }
      ) += (user_id, product_id)
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def update(id: Int, new_cart: Cart): Future[Unit] = {
    val cartToUpdate: Cart = new_cart.copy(id)
    db.run(cart.filter(_.id === id).update(cartToUpdate)).map(_ => ())
  }

  def delete(id: Int): Future[Int] = db.run(cart.filter(_.id === id).delete)

}
