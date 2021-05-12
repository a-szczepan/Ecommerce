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


}
case class CreateWishlistForm(user_id: Int, product_id: Int)

case class UpdateWishlistForm(id: Int = 0, user_id: Int, product_id: Int)