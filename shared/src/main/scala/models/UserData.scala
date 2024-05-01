package models

import play.api.libs.json.Json

sealed trait DataObject 
// case class JsonReturn(data: DataObject, expired: Boolean)
case class UserData(username: String, password: String, expired: Boolean = false) extends DataObject
case class UserMessage(username: String, message: String, expired: Boolean = false) extends DataObject

object ReadsAndWrites {
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val userMessageReads = Json.reads[UserMessage]
  implicit val userMessageWrites = Json.writes[UserMessage]
  // implicit val jsonReturnReads = Json.reads[JsonReturn]
  // implicit val jsonReturnWrites = Json.writes[JsonReturn]
}