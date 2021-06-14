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

    def cartId = column[Int]("cartId")

    def cartFk = foreignKey("cartId", cartId, cart)(_.id)

    def shippingId = column[Int]("shippingId")

    def shippingFk = foreignKey("shippingId", shippingId, shipping)(_.id)

    def * = (id, cartId, shippingId) <> ((Order.apply _).tupled, Order.unapply)
  }

  import cartRepository.CartTable
  import shippingRepository.ShippingTable

  val cart = TableQuery[CartTable]
  val shipping = TableQuery[ShippingTable]
  var order = TableQuery[OrderTable]

  def create(cartId: Int, shippingId: Int): Future[Order] = db.run {
    (order.map(o => (o.cartId, o.shippingId))
      returning order.map(_.id)
      into { case ((cartId, shippingId), id) => Order(id, cartId, shippingId) }
      ) += (cartId, shippingId)
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

  def getByCart(cartId: Int): Future[Seq[Order]] = db.run {
    order.filter(_.cartId === cartId).result
  }

  def getByShipping(shippingId: Int): Future[Seq[Order]] = db.run {
    order.filter(_.shippingId === shippingId).result
  }

}
