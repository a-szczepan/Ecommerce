package controllers
import models.{Wishlist, WishlistRepository, User, UserRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class WishlistFormController @Inject()(wishlistRepository: WishlistRepository,
                                      userRepository: UserRepository,
                                      productRepository: ProductRepository,
                                      cc: MessagesControllerComponents)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  var userList: Seq[User] = Seq[User]()
  var productList: Seq[Product] = Seq[Product]()

  userRepository.getAll().onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("", e)
  }

  productRepository.list().onComplete {
    case Success(product) => productList = product
    case Failure(e) => print("", e)
  }

  val wishlistForm: Form[CreateWishlistForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "productId" -> number,
    )(CreateWishlistForm.apply)(CreateWishlistForm.unapply)
  }

  val updateWishlistForm: Form[UpdateWishlistForm] = Form {
    mapping(
      "id" -> number,
      "providerKey" -> nonEmptyText,
      "productId" -> number,
    )(UpdateWishlistForm.apply)(UpdateWishlistForm.unapply)
  }

  /* -------------------------------------------- */

  def updateWishlist(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val wishlist = wishlistRepository.getById(id)
    wishlist.map(wishlist => {
      val wishlistForm = updateWishlistForm.fill(UpdateWishlistForm(wishlist.get.id, wishlist.get.providerKey, wishlist.get.productId))
      Ok(views.html.wishlist.updateWishlist(wishlistForm, userList, productList))
    })
  }

  def updateWishlistHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateWishlistForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.wishlist.updateWishlist(errorForm, userList, productList))
        )
      },
      wishlist => {
        wishlistRepository.update(wishlist.id, Wishlist(wishlist.id, wishlist.providerKey, wishlist.productId)).map { _ =>
          Redirect("/wishlist/all")
        }
      }
    )
  }

  def deleteWishlist(id: Int): Action[AnyContent] = Action {
    wishlistRepository.delete(id)
    Redirect("/wishlist")
  }

  def allWishlists: Action[AnyContent] = Action.async { implicit request =>
    wishlistRepository.list().map(wishlist => Ok(views.html.wishlist.allWishlists(wishlist)))
  }

  def getById(id: Int) = Action.async { implicit request =>
    val wishlist = wishlistRepository.getById(id)
    wishlist.map(wishlist => wishlist match {
      case Some(p) => Ok(views.html.wishlist.wishlistById(p))
      case None => Redirect(routes.WishlistFormController.allWishlists)
    })
  }

  def createWishlist() = Action { implicit request =>
    val users = Await.result(userRepository.getAll(), Duration.Inf)
    val products = Await.result(productRepository.list(), Duration.Inf)
    Ok(views.html.wishlist.createWishlist(wishlistForm, users, products))
  }

  def createWishlistHandle(): Action[AnyContent] = Action.async { implicit request =>
    wishlistForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.wishlist.createWishlist(errorForm, userList, productList))
        )
      },
      wishlist => {
        wishlistRepository.create(wishlist.providerKey, wishlist.productId).map { _ =>
          Redirect("/wishlist/all")
        }
      }
    )
  }

}
case class CreateWishlistForm(providerKey: String, productId: Int)

case class UpdateWishlistForm(id: Int = 0, providerKey: String, productId: Int)