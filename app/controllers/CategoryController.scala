package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CategoryController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createCategory: Action[AnyContent] = Action.async {
    Future{
      Ok("ok")
    }
  }

  def getCategories: Action[AnyContent] = Action.async {
    Future{
      Ok("")
    }
  }

  def getCategory(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def updateCategory(id: Long): Action[AnyContent] = Action async {
    Future{
      Ok("")
    }
  }

  def deleteCategory(id: Long): Action[AnyContent] = Action {
    NoContent
  }

}


