package controllers

import models.{Cart, CartRepository, Product, ProductRepository, User, UserRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CartFormController @Inject() (cartRepository: CartRepository,productRepository: ProductRepository, userRepository: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc){
  var userList: Seq[User] = Seq[User]()
  var productList: Seq[Product] = Seq[Product]()

  productRepository.list().onComplete {
    case Success(product) => productList = product
    case Failure(e) => print("", e)
  }

  userRepository.list().onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("", e)
  }

  val cartForm: Form[CreateCartForm] = Form {
    mapping(
      "user_id" -> number,
      "product_id" -> number,
    )(CreateCartForm.apply)(CreateCartForm.unapply)
  }

  val updateCartForm: Form[UpdateCartForm] = Form {
    mapping(
      "id" -> number,
      "user_id" -> number,
      "product_id" -> number,
    )(UpdateCartForm.apply)(UpdateCartForm.unapply)
  }

  /* -------------------------------------------- */

  def updateCart(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val cart = cartRepository.getById(id)
    cart.map(cart => {
      val cartForm = updateCartForm.fill(UpdateCartForm(cart.get.id, cart.get.user_id, cart.get.product_id))
      Ok(views.html.cart.updateCart(cartForm,userList,productList))
    })
  }

  def updateCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.cart.updateCart(errorForm, userList, productList))
        )
      },
      cart => {
        cartRepository.update(cart.id, Cart(cart.id, cart.user_id, cart.product_id)).map { _ =>
          Redirect("/carts/all")
        }
      }
    )
  }

  def deleteCart(id: Int): Action[AnyContent] = Action {
    cartRepository.delete(id)
    Redirect("/carts")
  }

  def allCarts: Action[AnyContent] = Action.async { implicit request =>
    cartRepository.list().map(carts => Ok(views.html.cart.allCarts(carts)))
  }

  def getById(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val cart = cartRepository.getById(id)
    cart.map {
      case Some(p) => Ok(views.html.cart.cartById(p))
      case None => Redirect(routes.CartFormController.allCarts)
    }
  }

  def createCart() = Action { implicit request =>
    val user = Await.result(userRepository.list(), Duration.Inf)
    val product = Await.result(productRepository.list(), Duration.Inf)
    Ok(views.html.cart.createCart(cartForm,user, product))
  }

  def createCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    cartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.cart.createCart(errorForm, userList, productList))
        )
      },
      cart => {
        cartRepository.create(cart.user_id, cart.product_id).map { _ =>
          Redirect("/carts/all")
        }
      }
    )
  }

}
case class CreateCartForm(user_id: Int, product_id: Int)
case class UpdateCartForm(id: Int = 0, user_id: Int, product_id: Int)
