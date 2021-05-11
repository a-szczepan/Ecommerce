package controllers
import models.{Product, Category, CategoryRepository, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductFormController @Inject() (productRepository: ProductRepository, categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){
  var categoryList: Seq[Category] = Seq[Category]()
  categoryRepository.list().onComplete{
    case Success(category) => categoryList = category
    case Failure(e) => print("error while listing categories", e)
  }

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "categoryId" -> number,
      "name" -> nonEmptyText,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> number,
      "categoryId" -> number,
      "name" -> nonEmptyText,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }


}

case class CreateProductForm(category_id: Int, name: String)
case class UpdateProductForm(id: Int = 0, category_id: Int, name: String)