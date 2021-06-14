package models

import play.api.libs.json.{Json, OFormat}

case class Shipping(id: Int = 0,providerKey: String,  streetName: String, buildingNumber: String, postalCode: String, city: String)

object Shipping {
  implicit val shippingFormat: OFormat[Shipping] = Json.format[Shipping]
}