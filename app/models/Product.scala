package models

import play.api.libs.json.{Json, OFormat}

case class Product(id: Int = 0, category_id: Int, name: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}