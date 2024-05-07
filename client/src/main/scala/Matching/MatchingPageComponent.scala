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
import _root_.playscala.FetchJson.fetchPost
import models.RequestStatus
import models.SwipeResult



@react class MatchingPageComponent extends Component {
    type Props = Unit
    case class State(currentIndex: Int)
    var profiles : Seq[ProfileData] = Nil
    def initialState = State(0)

    val matchRoute = document.getElementById("MatchRoute").asInstanceOf[html.Input].value
    val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
    val swipeRoute = document.getElementById("swipeRoute").asInstanceOf[org.scalajs.dom.html.Input].value
    implicit val ec = scala.concurrent.ExecutionContext.global

    val handleKeyPress = (event: KeyboardEvent) => {
      //swiping left and reject
      if (event.keyCode == 37) {

      
      } //swiping right and accept
      else if (event.keyCode == 39) { 
        //removing the swiped person from the list of users
        
        fetchPost(swipeRoute, csrfToken, profiles(state.currentIndex).username, 
          (sr: SwipeResult) => if (sr.expired) {

          } else if (sr.isMatched) {
              window.alert("You and this person matched! Go check your chat")
          } 
        )
        profiles = profiles.patch(state.currentIndex, Nil, 1)
      }

      //if you have exhausted the list, then go back to the beginning
      if(state.currentIndex >= profiles.length - 1){
          setState(state.copy(currentIndex = 0))
      }else{
          setState(state.copy(currentIndex = state.currentIndex+1))
      }
    }
  override def componentDidMount(): Unit = {
    document.addEventListener("keydown", handleKeyPress)
    FetchJson.fetchGet(matchRoute,
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
