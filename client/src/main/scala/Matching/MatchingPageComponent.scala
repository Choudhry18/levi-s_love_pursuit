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

@react class MatchingPageComponent extends Component {
  type Props = Unit
  case class State(profile: Option[Seq[ProfileData]], currentIndex: Int)
  def initialState = State(None, 0)

  val matchRoute = document.getElementById("MatchRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
  implicit val ec = scala.concurrent.ExecutionContext.global

  override def componentDidMount(): Unit = {
    FetchJson.fetchPost(matchRoute, csrfToken, "hehe",
      (matchContent: Seq[ProfileData]) => {
        setState(state.copy(profile = Some(matchContent)))
      }
    )
  }

  def render(): ReactElement = {
    val profileData = state.profile.getOrElse(Seq.empty)

    div(
        id := "matchingPage",
        state.profile match {
            case Some(profileData) if profileData.nonEmpty =>
            div(
                profileData.map { profile =>
                div(
                    key := profile.username,
                    div(
                    h3("Profile"),
                    p(s"Username: ${profile.username}"),
                    p(s"First Name: ${profile.firstName}"),
                    p(s"Last Name: ${profile.lastName}")
                    )
                )
                }
            )
            case _ =>
            p("No profiles found.")
        }
    )
  }
}