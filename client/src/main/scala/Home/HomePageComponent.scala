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

@react class HomePageComponent extends Component {
  type Props = Unit
  case class State(page: String)
  def initialState: State = State("Chat")

  implicit val ec = scala.concurrent.ExecutionContext.global

  lazy val chatPageComponent: ReactElement = ChatPageComponent()
  lazy val matchingPageComponenet: ReactElement = MatchingPageComponent()
  val pages: Map[String, ReactElement] =  Map("Matching" -> matchingPageComponenet, "Profile" -> chatPageComponent, "Chat" -> chatPageComponent)

  def selectPage(): ReactElement = pages.get(state.page)

  def render(): ReactElement = {
    div( id:="homePage",
      div( id:="navbar",
        div( id := "pageButtonsContainer",
          pages.keySet.zipWithIndex.map { case (pageName, i) =>
            button(key := i.toString(), className := "pageButton", id := {if (pageName == state.page) "selectedButton" else ""}, onClick := (e => {setState(state.copy(page = pageName))}), pageName)
          }
        )
      ),
      selectPage()
    )
  }

}