package models

import play.api.libs.json.Json

case class UserData(username: String, password: String)
case class UserMessage(message: String, username: String)

object ReadsAndWrites {
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
}