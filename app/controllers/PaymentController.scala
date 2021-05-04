package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createPayment: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getPayments: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getPayment(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deletePayment(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


