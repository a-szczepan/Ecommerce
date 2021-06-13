package models
import play.api.libs.json.{Json, OFormat}

case class Wishlist(id: Int = 0, providerKey: String, product_id: Int)

object Wishlist {
  implicit val wishlistFormat: OFormat[Wishlist] = Json.format[Wishlist]
}