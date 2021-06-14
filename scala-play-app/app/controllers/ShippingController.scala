package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import models.{Shipping, ShippingRepository, UserRepository}
import play.api.libs.json.{JsValue, Json}

@Singleton
class ShippingController @Inject()(shippingRepository: ShippingRepository, val userRepository: UserRepository,
                                   cc: ControllerComponents)
                                  (implicit ec: ExecutionContext)
  extends AbstractController(cc){

  def createShipping(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Shipping].map {
      shipping =>
        shippingRepository.create(shipping.providerKey, shipping.street_name, shipping.building_number, shipping.postal_code, shipping.city).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def getShippings: Action[AnyContent] = Action.async {
    val shippings = shippingRepository.list()
    shippings.map { shippings =>
      Ok(Json.toJson(shippings))
    }
  }

  def getShipping(id: Int): Action[AnyContent] = Action async {
    val shipping = shippingRepository.getById(id)
    shipping.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def getShippingByUserKey(providerKey: String): Action[AnyContent] = Action async {
    val shipping = shippingRepository.getByKey(providerKey)
    shipping.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateShipping(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Shipping].map {
      shipping =>
        shippingRepository.update(shipping.id, shipping).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteShipping(id: Int): Action[AnyContent] = Action.async {
    shippingRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}


