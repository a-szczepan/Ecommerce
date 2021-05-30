package controllers

import models.{Category, CategoryRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductFormController @Inject()(productRepository: ProductRepository, categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  var categoryList: Seq[Category] = Seq[Category]()

  categoryRepository.list().onComplete {
    case Success(category) => categoryList = category
    case Failure(e) => print("", e)
  }

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "category_id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "image" -> nonEmptyText,
      "price" -> nonEmptyText,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> number,
      "category_id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "image" -> nonEmptyText,
      "price" -> nonEmptyText,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  /* -------------------------------------------- */

  def updateProduct(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val product = productRepository.getById(id)
    product.map(product => {
      val productForm = updateProductForm.fill(UpdateProductForm(product.get.id, product.get.category_id, product.get.name, product.get.description, product.get.image, product.get.price))
      Ok(views.html.product.updateProduct(productForm, categoryList))
    })
  }

  def updateProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.product.updateProduct(errorForm, categoryList))
        )
      },
      product => {
        productRepository.update(product.id, Product(product.id, product.category_id, product.name, product.description, product.image, product.price)).map { _ =>
          Redirect("/products/all")
        }
      }
    )
  }

  def deleteProduct(id: Int): Action[AnyContent] = Action {
    productRepository.delete(id)
    Redirect("/products")
  }

  def allProducts: Action[AnyContent] = Action.async { implicit request =>
    productRepository.list().map(products => Ok(views.html.product.allProducts(products)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val product = productRepository.getById(id)
    product.map(product => product match {
      case Some(p) => Ok(views.html.product.productById(p))
      case None => Redirect(routes.ProductFormController.allProducts)
    })
  }

  def createProduct() = Action { implicit request =>
    val categories = Await.result(categoryRepository.list(), Duration.Inf)
    Ok(views.html.product.createProduct(productForm, categories))
  }

  def createProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.product.createProduct(errorForm, categoryList))
        )
      },
      product => {
        productRepository.create(product.category_id, product.name, product.description, product.image, product.price).map { _ =>
          Redirect("/products/all")
        }
      }
    )
  }

}

case class CreateProductForm(category_id: Int, name: String, description: String, image: String, price: String)

case class UpdateProductForm(id: Int = 0, category_id: Int, name: String, description: String, image: String, price: String)