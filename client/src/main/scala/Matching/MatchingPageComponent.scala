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

@react class MatchingPageComponent extends Component{
    type Props = Unit
    case class State(profile: Option[Seq[ProfileData]])
    def initialState = State(None)

    val matchRoute = document.getElementById("MatchRoute").asInstanceOf[html.Input].value
    val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
    implicit val ec = scala.concurrent.ExecutionContext.global

    override def componentDidMount(): Unit = {
        FetchJson.fetchPost(matchRoute, csrfToken, "hehe", 
        (matchContent : Seq[ProfileData]) => {
            print(matchContent.head.firstName)
            setState(state.copy(profile = Some(matchContent)))
        }
        )
    }
    def render(): ReactElement = {
        state.profile match {
        case Some(profileData) =>
            div(
            id := "matchingPage",
            div(
                h1("Profile Data"),
                p(s"Username: ${profileData.head.username}"),
                // Add more profile fields as needed
            )
            )
        case None =>
            div(
            id := "matchingPage",
            p("Loading...")
            )
        }
    }
}