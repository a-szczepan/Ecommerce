package controllers
import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ProductController @Inject()(cc: ControllerComponents) (implicit ec: ExecutionContext) extends AbstractController(cc){

  def createProduct: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getProducts: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getProduct(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateProduct(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


