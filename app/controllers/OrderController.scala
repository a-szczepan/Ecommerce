package controllers

import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrderController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createOrder: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getOrders: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getOrder(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateOrder(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


