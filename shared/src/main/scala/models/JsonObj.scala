package models

import play.api.libs.json.Json

case class UserData(username: String, password: String, expired: Boolean = false) 
case class UserMessage(username: String, message: String, expired: Boolean = false)
case class UserChats(chats: Seq[String], expired: Boolean = false)

object ReadsAndWrites {
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val userMessageReads = Json.reads[UserMessage]
  implicit val userMessageWrites = Json.writes[UserMessage]
  implicit val userChatsReads = Json.reads[UserChats]
  implicit val userChatsWrites = Json.writes[UserChats]
}