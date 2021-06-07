package controllers
import models.{Payment, PaymentRepository, Order, OrderRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class PaymentFormController @Inject()(paymentRepository: PaymentRepository,
                                      orderRepository: OrderRepository,
                                    cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  var orderList: Seq[Order] = Seq[Order]()

  orderRepository.list().onComplete {
    case Success(order) => orderList = order
    case Failure(e) => print("", e)
  }

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "order_id" -> number,
      "date" -> nonEmptyText,
      "amount" -> nonEmptyText,
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> number,
      "order_id" -> number,
      "date" -> nonEmptyText,
      "amount" -> nonEmptyText,
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  /* -------------------------------------------- */

  def deletePayment(id: Int): Action[AnyContent] = Action {
    paymentRepository.delete(id)
    Redirect("/payments")
  }

  def allPayments: Action[AnyContent] = Action.async { implicit request =>
    paymentRepository.list().map(payments => Ok(views.html.payment.allPayments(payments)))
  }

  def getById(paymentId: Int) = Action.async { implicit request =>
    val payment = paymentRepository.getById(paymentId)
    payment.map(payment => payment match {
      case Some(p) => Ok(views.html.payment.paymentById(p))
      case None => Redirect(routes.PaymentFormController.allPayments)
    })
  }

  def createPayment() = Action { implicit request =>
    val orders = Await.result(orderRepository.list(), Duration.Inf)
    Ok(views.html.payment.createPayment(paymentForm, orders))
  }

  def createPaymentHandle(): Action[AnyContent] = Action.async { implicit request =>
    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payment.createPayment(errorForm, orderList))
        )
      },
      payment => {
        paymentRepository.create(payment.order_id, payment.date, payment.amount).map { _ =>
          Redirect("/payments/all")
        }
      }
    )
  }

}
case class CreatePaymentForm(order_id: Int, date: String, amount: String)

case class UpdatePaymentForm(id: Int = 0, order_id: Int, date: String, amount: String)