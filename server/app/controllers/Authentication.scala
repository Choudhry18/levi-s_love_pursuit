package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.AuthenModel
import play.api.libs.json._
import models._

@Singleton
class Authentication @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
    implicit val userDataReads = Json.reads[UserData]
    def load = Action { implicit request =>
        Ok(views.html.authentication())
    }

  def withJsonBody[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => Redirect(routes.Authentication.load)
      }
    }.getOrElse(Redirect(routes.Authentication.load))
  }
  

  def validate() = Action{ implicit request =>
    withJsonBody[UserData]{ud => 
      if(AuthenModel.validateUser(ud.username, ud.password)){
        Ok(Json.toJson(true)).withSession("username"-> ud.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else{
        Ok(Json.toJson(false))
      }
    }
  }

  def createUser() = Action{ implicit request =>
    withJsonBody[UserData]{ud => 
      if(AuthenModel.createUser(ud.username, ud.password)){
        Ok(Json.toJson(true)).withSession("username"-> ud.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
      } else{
        Ok(Json.toJson(false))
      }
    }
  }

    def logout() = Action{ implicit request =>
        Ok(Json.toJson(true)).withNewSession
    }
}