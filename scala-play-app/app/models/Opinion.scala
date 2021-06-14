package models

import play.api.libs.json.{Json, OFormat}

case class Opinion(id: Int = 0, providerKey: String, productId: Int, opinionTxt: String)

object Opinion {
  implicit val opinionFormat: OFormat[Opinion] = Json.format[Opinion]
}