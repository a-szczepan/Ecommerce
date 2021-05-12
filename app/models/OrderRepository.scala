package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val cartRepository: CartRepository, val shippingRepository: ShippingRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "apporder") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def cart_id = column[Int]("cart_id")

    def cart_fk = foreignKey("cart_id", cart_id, cart)(_.id)

    def shipping_id = column[Int]("shipping_id")

    def shipping_fk = foreignKey("shipping_id", shipping_id, shipping)(_.id)

    def * = (id, cart_id, shipping_id) <> ((Order.apply _).tupled, Order.unapply)
  }

  import cartRepository.CartTable
  import shippingRepository.ShippingTable

  val cart = TableQuery[CartTable]
  val shipping = TableQuery[ShippingTable]
  var order = TableQuery[OrderTable]

  def create(cart_id: Int, shipping_id: Int): Future[Order] = db.run {
    (order.map(o => (o.cart_id, o.shipping_id))
      returning order.map(_.id)
      into { case ((cart_id, shipping_id), id) => Order(id, cart_id, shipping_id) }
      ) += (cart_id, shipping_id)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }

  def update(id: Int, new_order: Order): Future[Int] = {
    val orderToUpdate: Order = new_order.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(order.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def getByCart(cart_id: Int): Future[Seq[Order]] = db.run {
    order.filter(_.cart_id === cart_id).result
  }

  def getByShipping(shipping_id: Int): Future[Seq[Order]] = db.run {
    order.filter(_.shipping_id === shipping_id).result
  }

}
