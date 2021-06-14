package models

import play.api.libs.json.{Json, OFormat}

case class Order(id: Int = 0, cartId: Int, shippingId: Int)

object Order {
  implicit val orderFormat: OFormat[Order] = Json.format[Order]
}