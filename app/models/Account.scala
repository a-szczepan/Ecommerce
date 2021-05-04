package models

import play.api.libs.json.{Json, OFormat}

case class Account(id: Int = 0, user_id: Int, first_name: String, last_name: String)

object Account {
  implicit val accountFormat: OFormat[Account] = Json.format[Account]
}