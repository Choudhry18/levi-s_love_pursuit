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
import models.UserMessage
import models.RequestStatus
import models.ChatContent



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

@react class ChatComponent extends Component {
  case class Props(recipient: String, socket: WebSocket)
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
      (chatContent : ChatContent) => {
        if (!chatContent.expired) {
          println(chatContent)
          setState(state.copy(chat = chatContent.content))
        } else {
          window.location.assign(loginRoute)
        }
      }
    )
  }

  override def componentDidMount(): Unit = {
    props.socket.onmessage = {e => {
      println("got a text")
      fetchChatContent()
    }}
    fetchChatContent()
    //scroll down to newest message
    val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
    chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
  }

  //activate whenever a new message has been sent
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    // Fetch new chat content when recipient changes
    if (props.recipient != prevProps.recipient) {
      //clear current message when recipient changes
      setState(state.copy(newMessage = ""))
      //so there's no glitch while chat content is being fetched
      setState(state.copy(chat = Nil))
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
        (status : RequestStatus) => 
          if (!status.expired) {
            {if (status.success) {
            //sending the recipient's socket a message to update their chat
            props.socket.send(props.recipient)
            fetchChatContent()
            val chatContentScrollSection = document.getElementById("chatContent").asInstanceOf[org.scalajs.dom.html.Input]
            chatContentScrollSection.scrollTop = chatContentScrollSection.scrollHeight
            } else println("failed to send message")} 
          } else {
            window.location.assign(loginRoute)
          }
      )
      setState(state.copy(newMessage = ""))
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
      input( `type` := "text", value := state.newMessage, id := "chatInput", placeholder := "TYPE...",
        onChange := (e => {
          setState(state.copy(newMessage = e.target.value))
        }),
        onKeyDown := (e => sendMessage(e.key))
      )
    )
  }

}