package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div

import org.scalajs.dom.document
import org.scalajs.dom.html
import slinky.web.html._

import play.api.libs.json.JsError
import org.scalajs.dom._
import play.api.libs.json.Json
import scala.scalajs.js
import slinky.core.SyntheticEvent

@react class ChatComponent extends Component {
  case class Props(recipient: String)
  case class State(chat: Seq[(String, String)], newMessage : String)
  implicit val ec = scala.concurrent.ExecutionContext.global

  val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
  val getChatContentRoute = document.getElementById("getChatContentRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val chatsRoute = document.getElementById("chatsRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val sendMessageRoute = document.getElementById("sendMessageRoute").asInstanceOf[org.scalajs.dom.html.Input].value


  def fetchChatContent(): Unit = {
    BackendFetch.fetchPost(getChatContentRoute, csrfToken, props.recipient, 
      (chatContent) => {
        setState(state.copy(chat = BackendFetch.parseChatContent(chatContent)))
      })
  }
  def initialState: State = State(Nil, "")

  override def componentDidMount(): Unit = {
    fetchChatContent()
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    // Fetch new chat content when recipient changes
    if (props.recipient != prevProps.recipient) {
      fetchChatContent()
    }
  }
  
  //TODO: display something if your chat is empty

  def handleSubmit(key : String) : Unit = {
    //TODO handle errors somehow
    if (key == "Enter") {
      // println("Sending " + state.newMessage)
      BackendFetch.fetchPost(sendMessageRoute, csrfToken, (props.recipient, state.newMessage).toString(), 
        (success) => {if (BackendFetch.parseBoolean(success)) {fetchChatContent()}
          else println("couldn't send message")
        }
      )
      setState(state.copy(newMessage = ""))
      // val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
      // // chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
      // // println(chatContentScrollSection.scrollTop)
    }
  }

  def render(): ReactElement = {
    div (id := "chatBoxSection")(
      div( id := "chatBox",
        h1( id:= "recipientName", props.recipient),
        div ( id:= "chatContent",
          state.chat.zipWithIndex.map {case (message, i) => 
            if (message._1 == props.recipient) {
              div(key := i.toString, className := "recipientMessage", div(className := "message", message._2))
            } else {
              div(key := i.toString, className := "userMessage", div(className := "message", message._2))
            }
          }
        ),
        
      ),
      input( `type` := "text", value := state.newMessage, id := "chatInput",
        onChange := (e => setState(state.copy(newMessage = e.target.value))),
        onKeyDown := (e => handleSubmit(e.key))
      )
    )
  }

}