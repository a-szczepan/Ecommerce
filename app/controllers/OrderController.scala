package controllers

import models.{Order, OrderRepository, CartRepository, ShippingRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}



@Singleton
class OrderController @Inject()(orderRepository: OrderRepository,
                                cartRepository: CartRepository,
                                shippingRepository: ShippingRepository,
                                cc: ControllerComponents)
                               (implicit ec: ExecutionContext)
  extends AbstractController(cc){

  def createOrder: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Order].map {
        order =>
          orderRepository.create(order.cart_id, order.shipping_id).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getOrders: Action[AnyContent] = Action.async {
    val orders = orderRepository.list()
    orders.map{
      orders => Ok(Json.toJson(orders))
    }
  }

  def getOrder(id: Int): Action[AnyContent] = Action.async {
    val order = orderRepository.getById(id)
    order.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateOrder(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Order].map {
      order =>
        orderRepository.update(order.id, order).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteOrder(id: Int): Action[AnyContent] = Action.async {
    orderRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getOrdersByCart(cart_id: Int): Action[AnyContent] = Action.async{
    val orders = orderRepository.getByCart(cart_id)
    orders.map {
      orders => Ok(Json.toJson(orders))
    }
  }

  def getOrdersByShipping(shipping_id: Int): Action[AnyContent] = Action.async{
    val orders = orderRepository.getByShipping(shipping_id)
    orders.map {
      orders => Ok(Json.toJson(orders))
    }
  }

}


