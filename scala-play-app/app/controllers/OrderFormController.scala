package controllers
import models.{Order, OrderRepository, Cart, CartRepository, Shipping, ShippingRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OrderFormController @Inject()(orderRepository: OrderRepository,
                                    cartRepository: CartRepository,
                                    shippingRepository: ShippingRepository,
                                    cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  var cartList: Seq[Cart] = Seq[Cart]()
  var shippingList: Seq[Shipping] = Seq[Shipping]()

  cartRepository.list().onComplete {
    case Success(cart) => cartList = cart
    case Failure(e) => print("", e)
  }

  shippingRepository.list().onComplete {
    case Success(shipping) => shippingList = shipping
    case Failure(e) => print("", e)
  }

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "cart_id" -> number,
      "shipping_id" -> number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> number,
      "cart_id" -> number,
      "shipping_id" -> number,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  /* -------------------------------------------- */

  def updateOrder(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val order = orderRepository.getById(id)
    order.map(order => {
      val orderForm = updateOrderForm.fill(UpdateOrderForm(order.get.id, order.get.cart_id, order.get.shipping_id))
      Ok(views.html.order.updateOrder(orderForm, cartList, shippingList))
    })
  }

  def updateOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateOrderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.order.updateOrder(errorForm, cartList, shippingList))
        )
      },
      order => {
        orderRepository.update(order.id, Order(order.id, order.cart_id, order.shipping_id)).map { _ =>
          Redirect("/orders/all")
        }
      }
    )
  }

  def deleteOrder(id: Int): Action[AnyContent] = Action {
    orderRepository.delete(id)
    Redirect("/orders")
  }

  def allOrders: Action[AnyContent] = Action.async { implicit request =>
    orderRepository.list().map(orders => Ok(views.html.order.allOrders(orders)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val order = orderRepository.getById(id)
    order.map(order => order match {
      case Some(p) => Ok(views.html.order.orderById(p))
      case None => Redirect(routes.OrderFormController.allOrders)
    })
  }

  def createOrder() = Action { implicit request =>
    val carts = Await.result(cartRepository.list(), Duration.Inf)
    val shippings = Await.result(shippingRepository.list(), Duration.Inf)
    Ok(views.html.order.createOrder(orderForm, carts, shippings))
  }

  def createOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.order.createOrder(errorForm, cartList, shippingList))
        )
      },
      order => {
        orderRepository.create(order.cart_id, order.shipping_id).map { _ =>
          Redirect("/orders/all")
        }
      }
    )
  }

}
case class CreateOrderForm(cart_id: Int, shipping_id: Int)

case class UpdateOrderForm(id: Int = 0, cart_id: Int, shipping_id: Int)