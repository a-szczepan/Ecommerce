package controllers

import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createAccount: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getAccounts: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getAccount(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateAccount(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteAccount(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


