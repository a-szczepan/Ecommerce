package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def order_id = column[Int]("order_id")

    def order_fk = foreignKey("order_id", order_id, order)(_.id)

    def date = column[String]("date")

    def amount = column[Float]("amount")

    def * = (id, order_id, date, amount) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  import orderRepository.OrderTable

  val order = TableQuery[OrderTable]
  val payment = TableQuery[PaymentTable]

  def create(order_id: Int, date: String, amount: Float): Future[Payment] = db.run {
    (payment.map(o => (o.order_id, o.date, o.amount))
      returning payment.map(_.id)
      into { case ((order_id, date, amount), id) => Payment(id, order_id, date, amount) }
      ) += (order_id, date, amount)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def update(id: Int, new_payment: Payment): Future[Int] = {
    val paymentToUpdate: Payment = new_payment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(payment.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

}
