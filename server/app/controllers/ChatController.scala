package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import models.UserMessage
import models.ReadsAndWrites._


@Singleton
class ChatController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def withJsonBody[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => {
          println("ERROR OCCURED TRYING TO PARSE JSON")
          Redirect(routes.Authentication.load)
        }
      }
    }.getOrElse{
      println("ERROR OCCURED TRYING TO PARSE JSON")
      Redirect(routes.Authentication.load)
    }
  }
  
  def withSessionUsername(f: String => Result)(expireProcess: Result)(implicit request: Request[AnyContent]) = {
    request.session.get("username").map(f).getOrElse(expireProcess)
  }
  
  def load = Action { implicit request =>
    Ok(views.html.chat())
  }

  //TODO: make sure user have session and get their username from session
  def chats = Action { implicit request =>
    withSessionUsername{ username => 
      val userChats : Seq[String] = models.ChatModel.getChats(username)
      Ok(Json.toJson(userChats))
    }(Ok(Json.toJson(Seq.empty[String])))
  }

  def getChatContent = Action { implicit request =>
    withSessionUsername{ username =>
      withJsonBody[String]{ recipient =>
        val chatContent : Seq[UserMessage] = models.ChatModel.getChatContent(username, recipient) 
        Ok(Json.toJson(chatContent))
      }
    }(Ok(Json.toJson(Seq.empty[String])))
  }

  def sendMessage = Action { implicit request =>
    withSessionUsername{ sender => 
      withJsonBody[UserMessage] { um =>
        models.ChatModel.addMessage(sender, um.username, um.message) 
        Ok(Json.toJson(true))
      }
    }(Ok(Json.toJson(false)))
  }
}
