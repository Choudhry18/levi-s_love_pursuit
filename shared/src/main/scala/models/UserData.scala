package models

import play.api.libs.json.Json

case class UserData(username: String, password: String)
case class UserMessage(username: String, message: String)

object ReadsAndWrites {
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val userMessageReads = Json.reads[UserMessage]
  implicit val userMessageWrites = Json.writes[UserMessage]

}