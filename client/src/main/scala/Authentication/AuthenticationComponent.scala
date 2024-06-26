package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.window
import slinky.web.html._

//for serializing user data
import models.ReadsAndWrites._


@react class AuthenticationComponent extends Component {
  type Props = Unit
  case class State(loginName : String, loginPass : String, createEmail: String, createName : String, createPass : String)
  def initialState: State = State("", "", "", "", "")

  implicit val ec = scala.concurrent.ExecutionContext.global
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
  val validateRoute = document.getElementById("validateRoute").asInstanceOf[html.Input].value
  val createRoute = document.getElementById("createRoute").asInstanceOf[html.Input].value
  val homePageRoute = document.getElementById("homePageRoute").asInstanceOf[html.Input].value
  val onboardingRoute = document.getElementById("onboardingRoute").asInstanceOf[html.Input].value

  def login(): Unit = {
    val data = models.UserData("", state.loginName, state.loginPass)
    FetchJson.fetchPost(validateRoute, csrfToken, data, (bool : Boolean) => {
      if (bool) {
        println("worked")
        window.location.assign(homePageRoute)
      } else {
        window.alert("Wrong user name or password")
      }
    })
  }

  def createUser(): Unit = {
    val data = models.UserData(state.createEmail, state.createName, state.createPass)
    FetchJson.fetchPost(createRoute, csrfToken, data, (bool : Boolean) => {
      if (bool) {
        println("worked")
        window.location.assign(onboardingRoute)
      } else {
        window.alert("Username or email already exist")
      }
    })
  }

  def render(): ReactElement = {
    div(id:="login-section",
      h1("Login"),
      br(),
      span("Username: "),
      input(`type`:="text", id:="loginName", onChange := (e => setState(state.copy(loginName = e.target.value)))),
      span("Password: "),
      input(`type`:="password", id:="loginPass", onChange := (e => setState(state.copy(loginPass = e.target.value)))),
      button("Submit", onClick := (e => login())),
      h1("Create User"),
      br(),
      button("Create New Account", onClick := (e => window.location.assign(onboardingRoute))),
    )
  }                          
}