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



@react class ChatPageComponent extends Component {
  type Props = Unit
  case class State(chats: Seq[String], chatContentRecipient: String)
  def initialState: State = State(Nil, "")

  implicit val ec = scala.concurrent.ExecutionContext.global

  val chatsRoute = document.getElementById("chatsRoute").asInstanceOf[html.Input].value
  val chatBarImgRoute = document.getElementById("chatBarImgRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val loginRoute = document.getElementById("loginRoute").asInstanceOf[org.scalajs.dom.html.Input].value


  override def componentDidMount(): Unit = {
    FetchJson.fetchGet(chatsRoute, 
    (jsonChats : UserChats) => 
      if (jsonChats.expired){
        //reroutes back to home page if session has expired
        window.location.assign(loginRoute)
      } else {setState(state.copy(chats = jsonChats.chats, chatContentRecipient = jsonChats.chats(0)))})
      
  }
  
  //TODO: display something if your chat is empty

  def render(): ReactElement = {
    div( id:= "chatPage",
      div( id := "searchBarContainer"),
      div( id := "chatBarsContainer",
        state.chats.zipWithIndex.map { case (recipient, i) => 
          div(key := i.toString(), className := "chatBar",
            img(className := "chatBarbubble", src := chatBarImgRoute, key := i.toString(), onClick := (e => {setState(state.copy(chatContentRecipient = recipient))})),
            div(className := "chatBarName", recipient)
          )
        }
      ),
      ChatComponent(state.chatContentRecipient)
    )
  }

}