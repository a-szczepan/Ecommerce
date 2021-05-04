package models

import play.api.libs.json.{Json, OFormat}

case class Opinion(id: Int = 0, user_id: Int, product_id: Int, opinion_txt: String)

object Opinion {
  implicit val opinionFormat: OFormat[Opinion] = Json.format[Opinion]
}