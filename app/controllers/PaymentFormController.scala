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


}
case class CreatePaymentForm(cart_id: Int, shipping_is: Int)

case class UpdatePaymentForm(id: Int = 0, cart_id: Int, shipping_is: Int)