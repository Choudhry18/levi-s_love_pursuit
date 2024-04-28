package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import play.api.libs._
import models._
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import models.UserData

@Singleton
class TaskList4 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
    def load = Action{ implicit request =>
        Ok(views.html.version4Main())
    }

    implicit val userDataReads = Json.reads[UserData]

    def validate() = Action{ implicit request =>
        request.body.asJson.map { body =>
            Json.fromJson[UserData](body) match{
                case JsSuccess(ud, path) =>
                    if(TaskListInMemoryModel.validateUser(ud.username, ud.password)){
                        Ok(Json.toJson(true))
                        .withSession("username"-> ud.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
                    } else{
                        Ok(Json.toJson(false))
                    }
                case e @ JsError(_) => Redirect("@routes.TaskList4.load")
            }
        }.getOrElse(Redirect("@routes.TaskList4.load"))
    }

    def createUser() = Action{ implicit request =>
        request.body.asJson.map { body =>
            Json.fromJson[UserData](body) match{
                case JsSuccess(ud, path) =>
                    if(TaskListInMemoryModel.createUser(ud.username, ud.password)){
                        Ok(Json.toJson(true))
                        .withSession("username"-> ud.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
                    } else{
                        Ok(Json.toJson(false))
                    }
                case e @ JsError(_) => Redirect("@routes.TaskList4.load")
            }
        }.getOrElse(Redirect("@routes.TaskList4.load"))
    }

    def logout() = Action{ implicit request =>
        Ok(Json.toJson(true)).withNewSession
    }
}