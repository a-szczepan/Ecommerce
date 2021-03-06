package models

import play.api.libs.json.{Json, OFormat}

case class Payment(id: Int = 0, orderId: Int, date: String, amount: String)

object Payment {
  implicit val paymentFormat: OFormat[Payment] = Json.format[Payment]
}