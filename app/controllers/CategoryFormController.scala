package controllers
import models.{Category,CategoryRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryFormController @Inject()(categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def updateCategory(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepository.getById(id)
    category.map(category => {
      val categoryForm = updateCategoryForm.fill(UpdateCategoryForm(category.get.id, category.get.name))
      Ok(views.html.category.updateCategory(categoryForm))
    })
  }

  def updateCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.category.updateCategory(errorForm))
        )
      },
      category => {
        categoryRepository.update(category.id, Category(category.id, category.name)).map { _ =>
          Redirect("/categories")
        }
      }
    )
  }

  def deleteCategory(id: Int): Action[AnyContent] = Action {
    categoryRepository.delete(id)
    Redirect("/categories")
  }

  def allCategories: Action[AnyContent] = Action.async { implicit request =>
    categoryRepository.list().map(categories => Ok(views.html.category.allCategories(categories)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val category = categoryRepository.getById(id)
    category.map(category => category match {
      case Some(p) => Ok(views.html.category.categoryById(p))
      //case None => Redirect(routes.CategoryFormController.allCategories)
    })
  }

  def createCategory(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepository.list()
    categories.map(_ => Ok(views.html.category.createCategory(categoryForm)))
  }

  def createCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.category.createCategory(errorForm))
        )
      },
      category => {
        categoryRepository.create(category.name).map { _ =>
          Redirect("/categories/all")
        }
      }
    )
  }
}

case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(id: Int, name: String)