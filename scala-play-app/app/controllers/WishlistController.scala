package controllers

import models.{Wishlist, WishlistRepository, UserRepository, ProductRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WishlistController @Inject()(val wishlistRepository: WishlistRepository,
                                   val userRepository: UserRepository,
                                   val productRepository: ProductRepository,
                                cc: ControllerComponents)
                               (implicit ec: ExecutionContext)
  extends AbstractController(cc){

  def createWishlist(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Wishlist].map {
        wishlist =>
          wishlistRepository.create(wishlist.user_id, wishlist.product_id).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getWishlists: Action[AnyContent] = Action.async {
    val wishlists = wishlistRepository.list()
    wishlists.map{
      wishlists => Ok(Json.toJson(wishlists))
    }
  }

  def getWishlist(id: Int): Action[AnyContent] = Action.async {
    val wishlist = wishlistRepository.getById(id)
    wishlist.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateWishlist(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Wishlist].map {
      wishlist =>
        wishlistRepository.update(wishlist.id, wishlist).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteWishlist(id: Int): Action[AnyContent] = Action.async {
    wishlistRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getWishlistByUser(user_id: Int): Action[AnyContent] = Action.async{
    val wishlists = wishlistRepository.getByUser(user_id)
    wishlists.map {
      wishlists => Ok(Json.toJson(wishlists))
    }
  }

  def getWishlistByProduct(product_id: Int): Action[AnyContent] = Action.async{
    val wishlists = wishlistRepository.getByProduct(product_id)
    wishlists.map {
      wishlists => Ok(Json.toJson(wishlists))
    }
  }


}


