package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import _root_.controllers.controllers.MainController


@Singleton
class ChatController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
    // implicit val stringReads = Json.reads[String]


    //TODO: error handling
    def withJsonBodyInMemory[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => Redirect(routes.ChatController.load)
      }
    }.getOrElse(Redirect(routes.ChatController.load))
    }
    def load = Action { implicit request =>
        Ok(views.html.chat())
    }

    def chats = Action { implicit request =>
        //TODO: make sure user have session and get their username from session
        Ok(Json.toJson(models.ChatModel.getChats("Levi")))
    }

    def getChatContent = Action { implicit request =>
        val data = request.body.asText.getOrElse("")
        val chatContent : Seq[(String, String)] = models.ChatModel.getChatContent("Levi", data) 
        Ok(Json.toJson(models.ChatModel.getChatContent("Levi", data)))
        //TODO: make sure user have session and get their username from session
    }

    def sendMessage = Action { implicit request =>
      val data = request.body.asText.getOrElse("")
      val MessageRecipientRegex = """\(([^"]+),([^"]+)\)""".r
      MessageRecipientRegex.findFirstMatchIn(data) match {
        case Some(MessageRecipientRegex(recipient, message)) => {
          models.ChatModel.addMessage("Levi", recipient, message) 
          Ok(Json.toJson(true))
        }
        case None => Ok(Json.toJson(false))
      }
    }
}
