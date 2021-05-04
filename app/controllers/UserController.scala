package controllers
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createUser: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getUsers: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getUser(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateUser(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteUser(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


