package models

import play.api.libs.json.{Json, OFormat}

case class Shipping(id: Int = 0, street_name: String, building_number: Int, apartment_number: Int, postal_code: String, city: String)

object Shipping {
  implicit val shippingFormat: OFormat[Shipping] = Json.format[Shipping]
}