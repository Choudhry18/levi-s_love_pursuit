package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.json._
import models.UserMessage
import models.ReadsAndWrites._

//database
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import models.UserChats
import models.ChatContent
import models.RequestStatus


@Singleton
class ChatController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
    extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
  private val dbModel = new models.ChatModel(db) 
  def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]) : Future[Result] = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => {
          println("ERROR OCCURED TRYING TO PARSE JSON")
          Future.successful(Redirect(routes.HomeController.load))
        }
      }
    }.getOrElse{
      println("ERROR OCCURED TRYING TO PARSE JSON")
      Future.successful(Redirect(routes.HomeController.load))
    }
  }
  
  def withSessionUsername(f: String => Future[Result])(expireProcess: Result)(implicit request: Request[AnyContent]) = {
    request.session.get("username").map(f).getOrElse(Future.successful(expireProcess))
  }
  
  // def load = Action { implicit request =>
  //   Ok(views.html.chat())
  // }

  def chats = Action.async { implicit request =>
    withSessionUsername{ username => 
      dbModel.getChats(username).map(userChats => Ok(Json.toJson(UserChats(userChats))))
    }(Ok(Json.toJson(UserChats(Nil, expired = true))))
  }

  //TODO: session expire handle
  def getChatContent = Action.async { implicit request =>
    withSessionUsername{ username =>
      withJsonBody[String]{ recipient =>
        dbModel.getChatContent(username, recipient).map(chatContent => Ok(Json.toJson(ChatContent(chatContent))))
      }
    }(Ok(Json.toJson(ChatContent(Nil, true))))
  }

  def sendMessage = Action.async { implicit request =>
    withSessionUsername{ sender => 
      withJsonBody[UserMessage] { um =>
        dbModel.addMessage(sender, um.username, um.message) 
        Future.successful(Ok(Json.toJson(RequestStatus(true))))
      }
    }(Ok(Json.toJson(RequestStatus(false, true))))
  }
}
