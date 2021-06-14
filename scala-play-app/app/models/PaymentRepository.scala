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

    def orderId = column[Int]("orderId")

    def orderFk = foreignKey("orderId", orderId, order)(_.id)

    def date = column[String]("date")

    def amount = column[String]("amount")

    def * = (id, orderId, date, amount) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  import orderRepository.OrderTable

  val order = TableQuery[OrderTable]
  val payment = TableQuery[PaymentTable]

  def create(orderId: Int, date: String, amount: String): Future[Payment] = db.run {
    (payment.map(o => (o.orderId, o.date, o.amount))
      returning payment.map(_.id)
      into { case ((orderId, date, amount), id) => Payment(id, orderId, date, amount) }
      ) += (orderId, date, amount)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def update(id: Int, newPayment: Payment): Future[Int] = {
    val paymentToUpdate: Payment = newPayment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate))
  }

  def delete(id: Int): Future[Int] = db.run(payment.filter(_.id === id).delete)

  def getById(id: Int): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def getByOrder(orderId: Int): Future[Seq[Payment]] = db.run {
    payment.filter(_.orderId === orderId).result
  }

}
