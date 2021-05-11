package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import models.{Category, CategoryRepository}
import play.api.libs.json.{JsValue, Json}

@Singleton
class CategoryController @Inject()(val categoryRepository: CategoryRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc){

  def createCategory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Category].map {
      category =>
        categoryRepository.create(category.name).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def getCategories: Action[AnyContent] = Action.async {
    val categories = categoryRepository.list()
    categories.map { categories =>
      Ok(Json.toJson(categories))
    }
  }

  def getCategory(id: Int): Action[AnyContent] = Action async {
    val category = categoryRepository.getById(id)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateCategory(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Category].map {
      category =>
        categoryRepository.update(category.id, category).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteCategory(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}


