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
class PreferenceController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
    extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
  private val dbModel = new AuthenModel(db)

  def load = Action { implicit request =>
      Ok(views.html.onboarding())
  }

  def withJsonBody[A](f: A => Future[Result])(implicit reads: Reads[A], ec: ExecutionContext): Action[AnyContent] = {
    Action.async { implicit request =>
        request.body.asJson match {
        case Some(json) => json.validate[A] match {
            case JsSuccess(value, _) => f(value)
            case JsError(errors) =>
            println("Failed to validate JSON: " + errors)
            Future.successful(BadRequest("Invalid JSON format"))
        }
        case None =>
            println("Expecting JSON data")
            Future.successful(BadRequest("Expecting JSON data"))
        }
    }
    }
  
    private val preferenceModel = new PreferenceModel(db)

    def createPreference() = withJsonBody[PreferenceData] { pd =>
        preferenceModel.createPreference(pd).map { success =>
        if (success) {
            Ok(Json.toJson(true))
        } else {
            Ok(Json.toJson(false))
        }
        }
    }

}