package models

import play.api.libs.json.{Json, OFormat}

case class Account(id: Int = 0, providerKey: String, firstName: String, lastName: String)

object Account {
  implicit val accountFormat: OFormat[Account] = Json.format[Account]
}