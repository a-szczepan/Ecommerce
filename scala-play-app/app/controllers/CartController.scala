package controllers

import models.{Cart, CartRepository, ProductRepository, UserRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CartController @Inject()(val cartRepository: CartRepository,
                               val productRepository: ProductRepository,
                               val userRepository: UserRepository,
                               cc: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def createCart: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Cart].map {
        cart =>
          cartRepository.create(cart.providerKey,cart.productId,cart.quantity).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getCarts: Action[AnyContent] = Action.async {
    val carts = cartRepository.list()
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def getCartsByProduct(productId: Int): Action[AnyContent] = Action.async {
    val carts = cartRepository.getByProductId(productId)
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def getCartsByUserKey(providerKey: String): Action[AnyContent] = Action.async {
    val carts = cartRepository.getByUserKey(providerKey)
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def updateCart(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Cart].map {
      cart =>
        cartRepository.update(cart.id, cart).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteCart(id: Int): Action[AnyContent] = Action.async {
    cartRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}


