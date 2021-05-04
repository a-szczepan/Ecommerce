package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ShippingController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createShipping: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getShipping: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }


  def updateShipping: Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteShipping: Action[AnyContent] = Action {
    NoContent
  }

}


