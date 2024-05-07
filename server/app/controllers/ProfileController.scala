package controllers

import javax.inject._

import play.api.mvc._

import models._
import models.ReadsAndWrites._
import play.api.libs.json._

//database
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import slick.jdbc.JdbcProfile
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import akka.util.ByteString


@Singleton
class ProfileController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext) 
    extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
  
  private val dbModel = new models.ProfileModel(db) 

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

  def getPhoto = Action.async { implicit request =>
    withSessionUsername( username =>
      dbModel.getPhoto(username).map(bArray => Ok(Json.toJson(bArray)))
    )(Ok(Json.toJson(Array.emptyByteArray)))
  }
}