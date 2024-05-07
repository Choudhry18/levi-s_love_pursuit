package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.window
import slinky.web.html._
import models.UserChats
import models.ReadsAndWrites._
import models.ProfileData

import org.scalajs.dom.KeyboardEvent



@react class MatchingPageComponent extends Component {
  type Props = Unit
  case class State(currentIndex: Int)
  var profiles : Seq[ProfileData] = Nil
  def initialState = State(0)

  val matchRoute = document.getElementById("MatchRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
  implicit val ec = scala.concurrent.ExecutionContext.global

  val handleKeyPress = (event: KeyboardEvent) => {
        setState(state.copy(currentIndex = state.currentIndex+1))
    }
  override def componentDidMount(): Unit = {
    document.addEventListener("keydown", handleKeyPress)
    FetchJson.fetchPost(matchRoute, csrfToken, "hehe",
      (matchContent: Seq[ProfileData]) => {
        profiles = matchContent
        forceUpdate()
      }
    )
  }

  def render(): ReactElement = {
    if (profiles.isEmpty) {
      div(
        id := "matchingPage",
        p("Loading...")
      )
    } else {
      div(
        id := "matchingPage",
        div(
          id := "profileDisplay", // ID for the profile display
          div(
            key := profiles(state.currentIndex).username,
            div(
              id := "profileContent", // ID for the profile content
              h3("Profile"),
              p(s"Username: ${profiles(state.currentIndex).username}"),
              p(s"First Name: ${profiles(state.currentIndex).firstName}"),
              p(s"Last Name: ${profiles(state.currentIndex).lastName}")
            )
          )
        )
      )
    }
  }

}
