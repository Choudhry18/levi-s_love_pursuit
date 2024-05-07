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
import models.ProfileData
import scala.util.{Success, Failure}
import models.SwipeResult

@Singleton
class MatchingController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
    private val dbModel = new models.MatchModel(db) 
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

    def getProfiles = Action.async {implicit request =>
        withSessionUsername{ username =>
            dbModel.getProfiles(username).map(profiles => Ok(Json.toJson(profiles)))
        }(Ok(Json.toJson(UserChats(Nil, expired = true))))
    }

    def swipe = Action.async {implicit request =>
        withSessionUsername{ username =>
          withJsonBody[String]( swipee => dbModel.sendSwipe(username, swipee).map(success => Ok(Json.toJson(SwipeResult(success, false)))))
        }(Ok(Json.toJson(SwipeResult(false, true))))
    }

}