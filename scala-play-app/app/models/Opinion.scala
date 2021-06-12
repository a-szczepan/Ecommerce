package models

import play.api.libs.json.{Json, OFormat}

case class Opinion(id: Int = 0, providerKey: String, product_id: Int, opinion_txt: String)

object Opinion {
  implicit val opinionFormat: OFormat[Opinion] = Json.format[Opinion]
}