package models
import play.api.libs.json.{Json, OFormat}

case class Wishlist(id: Int = 0, providerKey: String, productId: Int)

object Wishlist {
  implicit val wishlistFormat: OFormat[Wishlist] = Json.format[Wishlist]
}