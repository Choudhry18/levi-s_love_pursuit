package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.AuthenModel
import play.api.libs.json._
import models._
import models.ReadsAndWrites._

//database
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider


@Singleton
class Authentication @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
    extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
  private val dbModel = new AuthenModel(db)
  def load = Action { implicit request =>
      Ok(views.html.authentication())
  }

  def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]) : Future[Result] = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => {
          println("ERROR OCCURED TRYING TO PARSE JSON")
          Future.successful(Redirect(routes.Authentication.load))
        }
      }
    }.getOrElse{
      println("ERROR OCCURED TRYING TO PARSE JSON")
      Future.successful(Redirect(routes.Authentication.load))
    }
  }

  def validate() = Action.async { implicit request =>
    withJsonBody[UserData]{ud => 
      dbModel.validateUser(ud.username, ud.password).map { optionUserId =>
        optionUserId match {
          case Some(userid) => Ok(Json.toJson(true))
            .withSession("username" -> ud.username, "userId" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
          case None => Ok(Json.toJson(false))
        }
      }
    }
  }

  def createUser() = Action.async { implicit request =>
    withJsonBody[UserData]{ud => 
      dbModel.createUser(ud.username, ud.password, "arbemail@trinity.edu").map { optionUserId =>
        optionUserId match {
          case Some(userid) => Ok(Json.toJson(true))
            .withSession("username" -> ud.username, "userId" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
          case None => Ok(Json.toJson(false))
        }
      }
    }
  }

    def logout() = Action{ implicit request =>
        Ok(Json.toJson(true)).withNewSession
    }
}