package models

import play.api.libs.json.Json

case class UserData(username: String, password: String, expired: Boolean = false) 
case class UserMessage(username: String, message: String)
case class UserChats(chats: Seq[String], expired: Boolean = false)
case class ChatContent(content: Seq[UserMessage], expired: Boolean = false)
case class RequestStatus(success: Boolean, expired: Boolean = false)
case class PreferenceData(
  gender: String,
  year: String,
  greekPreference: String,
  religion: String,
  commitment: String,
  major: String
)
case class ProfileData(
  firstName: String,
  lastName: String,
  bio: String,
  photo: String,
  gender: String,
  year: String,
  greek_association: String,
  religion: String,
  commitment: String,
  major: String
)


object ReadsAndWrites {
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val userMessageReads = Json.reads[UserMessage]
  implicit val userMessageWrites = Json.writes[UserMessage]
  implicit val userChatsReads = Json.reads[UserChats]
  implicit val userChatsWrites = Json.writes[UserChats]
  implicit val preferenceDataReads = Json.reads[PreferenceData]
  implicit val preferenceDataWrites = Json.writes[PreferenceData]
  implicit val profileDataReads = Json.reads[ProfileData]
  implicit val profileDataWrites = Json.writes[ProfileData]
  implicit val chatContentReads = Json.reads[ChatContent]
  implicit val chatContentWrites = Json.writes[ChatContent]
  implicit val statusReads = Json.reads[RequestStatus]
  implicit val statusWrites = Json.writes[RequestStatus]
}