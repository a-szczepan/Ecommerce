package models

import play.api.libs.json.{Json, OFormat}

case class Cart(id: Int = 0, providerKey: String, productId: Int, quantity: Int)

object Cart {
  implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}