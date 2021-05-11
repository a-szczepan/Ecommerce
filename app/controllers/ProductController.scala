package controllers
import models.{Product, CategoryRepository, ProductRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductController @Inject()(val productRepository: ProductRepository,
                                  val categoryRepository: CategoryRepository,
                                  cc: ControllerComponents)
                                 (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def createProduct: Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Product].map {
        product =>
          productRepository.create(product.category_id, product.name).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getProducts: Action[AnyContent] = Action.async {
    val products = productRepository.list()
    products.map{
      products => Ok(Json.toJson(products))
    }
  }

  def getProduct(id: Int): Action[AnyContent] = Action.async {
    val product = productRepository.getById(id)
    product.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def getProductsByCategory(category_id: Int): Action[AnyContent] = Action.async{
    val products = productRepository.getByCategoryId(category_id)
    products.map {
      products => Ok(Json.toJson(products))
    }
  }

  def updateProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Product].map {
      product =>
        productRepository.update(product.id, product).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteProduct(id: Int): Action[AnyContent] = Action.async {
    productRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}


