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
import org.scalajs.dom.WebSocket



@react class ChatPageComponent extends Component {
  case class Props(socket: WebSocket)
  case class State(chats: Seq[String], filterChat: String, chatContentRecipient: String)
  def initialState: State = State(Nil, "", "")

  implicit val ec = scala.concurrent.ExecutionContext.global

  val chatsRoute = document.getElementById("chatsRoute").asInstanceOf[html.Input].value
  val chatBarImgRoute = document.getElementById("chatBarImgRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val searchGlassImgRoute = document.getElementById("searchGlassImgRoute").asInstanceOf[org.scalajs.dom.html.Input].value

  val loginRoute = document.getElementById("loginRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val socketRoute = document.getElementById("ws-route").asInstanceOf[html.Input].value

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
      div( id := "searchBarSection",
        div( id := "searchBarContainer", 
          img( id := "searchGlass", src := searchGlassImgRoute),
          input( id := "searchBar", onChange := (e => setState(state.copy(filterChat = e.target.value))))
        )
      ),
      div( id := "chatBarsContainer",
        state.chats.filter(recipient => if (state.filterChat == "") true else recipient.contains(state.filterChat))
        .zipWithIndex.map { case (recipient, i) => 
          div(key := i.toString(), className := "chatBar",
            img(className := "chatBarBubble", src := chatBarImgRoute, key := i.toString(), onClick := (e => {setState(state.copy(chatContentRecipient = recipient))})),
            div(className := "chatBarName", recipient)
          )
        }
      ),
      ChatComponent(state.chatContentRecipient, props.socket)
    )
  }

}