package Authentication

import models.UserData
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom._
import play.api.libs.json.Json
import scala.scalajs.js.Thenable.Implicits._
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import scala.concurrent.ExecutionContext
import scala.scalajs.js.annotation.JSExportTopLevel
import scalajs.js
import slinky.web.html.method
import slinky.web.svg.mode
import slinky.web.html.body
import models.ReadsAndWrites

object Version6{
    val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
    val validateRoute = document.getElementById("validateRoute").asInstanceOf[html.Input].value
    val createRoute = document.getElementById("createRoute").asInstanceOf[html.Input].value
    val logoutRoute = document.getElementById("logoutRoute").asInstanceOf[html.Input].value

    def init(): Unit = {
        println("In version 6.")
    }

    implicit val ec = ExecutionContext.global
    implicit val userDataReads = Json.writes[UserData]

    @JSExportTopLevel("login")
    def login(): Unit = {
        val username = document.getElementById("loginName").asInstanceOf[html.Input].value
        val password = document.getElementById("loginPass").asInstanceOf[html.Input].value
        val data = UserData(username, password)
        FetchJson.fetchPost(validateRoute, csrfToken, HttpMethod.POST, data, (bool: Boolean) => {
            if(bool){
                document.getElementById("login-section").asInstanceOf[js.Dynamic].hidden = true
                document.getElementById("task-section").asInstanceOf[js.Dynamic].hidden = false
                document.getElementById("login-message").innerHTML = ""
                document.getElementById("create-message").innerHTML = ""
            } else {
                document.getElementById("login-message").innerHTML = "Login Failed"
            }
        }, e => {
            println("Fetch error " + e)
        })
    }

    @JSExportTopLevel("createUser")
    def createUser(): Unit = {
        val username = document.getElementById("createName").asInstanceOf[html.Input].value
        val password = document.getElementById("createPass").asInstanceOf[html.Input].value
        val data = UserData(username, password)
        FetchJson.fetchPost(createRoute, csrfToken, HttpMethod.POST, data, (bool: Boolean) => {
            if(bool) {
                document.getElementById("login-section").asInstanceOf[js.Dynamic].hidden = true
                document.getElementById("task-section").asInstanceOf[js.Dynamic].hidden = false
                document.getElementById("login-message").innerHTML = ""
                document.getElementById("create-message").innerHTML = ""
                document.getElementById("createName").asInstanceOf[html.Input].value = ""
                document.getElementById("createPass").asInstanceOf[html.Input].value = ""
            } else {
                document.getElementById("create-message").innerHTML = "User Creation Failed"
            }
            }, e => {
                println("Fetch error: " + e)
            })
    }


    @JSExportTopLevel("logout")
        def logout(): Unit = {
                FetchJson.fetchGet(logoutRoute, (bool: Boolean) => {
                document.getElementById("login-section").asInstanceOf[js.Dynamic].hidden = false;
                document.getElementById("task-section").asInstanceOf[js.Dynamic].hidden = true;
            }, e => {
                println("Fetch error: " + e)
            })
        }
}