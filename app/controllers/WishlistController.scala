package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class WishlistController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addToWishlist: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getWishlist: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def updateWishlist(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteFromWishlist(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


