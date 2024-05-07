package controllers

import javax.inject._

import play.api.mvc._
import models.AuthenModel
import play.api.libs.json._
import models._
import models.ReadsAndWrites._

//database
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import akka.util.ByteString
import java.io.ByteArrayOutputStream


@Singleton
class OnboardingController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
    extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  def load = Action { implicit request =>
      Ok(views.html.onboarding())
  }

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
  
  private val dbModel = new OnboardingModel(db)

  def createPreference = Action.async { implicit request =>
    withSessionUsername{ username =>
      withJsonBody[PreferenceData] 
      { pd =>
          dbModel.createPreference(pd, username).map { success =>
            if (success) {
                Ok(Json.toJson(true))
            } else {
                Ok(Json.toJson(false))
            }
          }
      }
    }(Ok(Json.toJson(false)))
  }

  def createProfile = Action.async {implicit request =>
    withSessionUsername{ username =>
      withJsonBody[ProfileData] 
      { pd => 
        dbModel.createProfile(pd, username).map { success =>
          if (success) {
              Ok(Json.toJson(true))
          } else {
              Ok(Json.toJson(false))
          }
        }
      }
    }(Ok(Json.toJson(false)))
  }

  def uploadPhoto = Action.async {implicit request => 
    withSessionUsername{ username =>
      val multipartData = request.body.asMultipartFormData
      if (multipartData.isDefined) {
        val file = multipartData.get.file("fieldName")
        val tempFile = file.get.ref
        if (file.isDefined) {
          val inputStream = file.get.refToBytes(tempFile) match {
            case Some(bytes) => {
                val byteArray: Array[Byte] = bytes.toArray 
                dbModel.uploadPhoto(byteArray, username).map(bool => Ok(Json.toJson(bool)))
              }
            case None =>
          }
        }
      }
      Future.successful(Ok(Json.toJson(true)))
    }(Ok(Json.toJson(false)))
  }

}