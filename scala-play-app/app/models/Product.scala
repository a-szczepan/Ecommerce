package models

import play.api.libs.json.{Json, OFormat}

case class Product(id: Int = 0, categoryId: Int, name: String, description: String, image: String, price: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}