package models

import play.api.libs.json.{Json, OFormat}

case class User(id: Int = 0, account_id: Int, order_id: Int, wishlist_id: Int, cart_id: Int, email: String, password: String )

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}