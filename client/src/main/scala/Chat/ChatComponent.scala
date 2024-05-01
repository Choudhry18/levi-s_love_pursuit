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
import models.ReadsAndWrites._
import models.UserMessage

@react class ChatComponent extends Component {
  case class Props(recipient: String)
  case class State(chat: Seq[UserMessage], newMessage : String)
  def initialState: State = State(Nil, "")

  implicit val ec = scala.concurrent.ExecutionContext.global
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[org.scalajs.dom.html.Input].value
  val getChatContentRoute = document.getElementById("getChatContentRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val chatsRoute = document.getElementById("chatsRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val sendMessageRoute = document.getElementById("sendMessageRoute").asInstanceOf[org.scalajs.dom.html.Input].value
  val loginRoute = document.getElementById("loginRoute").asInstanceOf[org.scalajs.dom.html.Input].value


  //getting chat content by fetch post the recipient name
  def fetchChatContent(): Unit = {
    val data = props.recipient
    FetchJson.fetchPost(getChatContentRoute, csrfToken, data, 
      (chatContent : Seq[UserMessage]) => {
        //if illegal access or no session => redirect
        if (!chatContent.isEmpty) setState(state.copy(chat = chatContent)) else setState(state.copy(chat = Nil))
      }
    )
  }

  override def componentDidMount(): Unit = {
    fetchChatContent()
    //scroll down to newest message
    val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
    chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
  }

  //activate whenever a new message has been sent
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    // Fetch new chat content when recipient changes
    if (props.recipient != prevProps.recipient) {
      fetchChatContent()
    }
    //scroll down to newest message
    val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
    chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
  }
  
  //handling user sending messages
  def sendMessage(key : String) : Unit = {
    //TODO handle errors somehow
    if (key == "Enter") {
      // println("Sending " + state.newMessage)
      val data = UserMessage(props.recipient, state.newMessage)
      FetchJson.fetchPost(sendMessageRoute, csrfToken, data, 
        (bool : Boolean) => {if (bool) fetchChatContent() else println("failed to send message")} 
      )
      setState(state.copy(newMessage = ""))
      //scroll down to newest message
      val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
      chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
    }
  }

  def render(): ReactElement = {
    div (id := "chatBoxSection")(
      div( id := "chatBox",
        h1( id:= "recipientName", props.recipient),
        div ( id:= "chatContent",
          state.chat.zipWithIndex.map {case (message, i) => 
            if (message.username == props.recipient) {
              div(key := i.toString, className := "recipientMessage", div(className := "message", message.message))
            } else {
              div(key := i.toString, className := "userMessage", div(className := "message", message.message))
            }
          }
        ),
      ),
      input( `type` := "text", value := state.newMessage, id := "chatInput",
        onChange := (e => setState(state.copy(newMessage = e.target.value))),
        onKeyDown := (e => sendMessage(e.key))
      )
    )
  }

}