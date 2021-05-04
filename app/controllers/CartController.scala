package controllers
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CartController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def addToCart(id:Long): Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getCart: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def updateCart(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteFromCart(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


