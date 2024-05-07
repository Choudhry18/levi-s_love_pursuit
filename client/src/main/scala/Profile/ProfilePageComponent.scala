package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div
import org.scalajs.dom.document
import org.scalajs.dom.html
import _root_.playscala.FetchJson.fetchPost

@react class ProfilePageComponent extends Component {
  type Props = Unit
  case class State(photoBuffer: Array[Byte])
  implicit val ec = scala.concurrent.ExecutionContext.global
  def initialState: State = State(Array.emptyByteArray)
  val getPhotoRoute = document.getElementById("getPhotoRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value

  override def componentDidMount(): Unit = {
    FetchJson.fetchPost(getPhotoRoute, csrfToken, "", (bArray: Array[Byte]) => {
      setState(state.copy(photoBuffer = bArray))  
    }
    )
  }

  def render(): ReactElement = {
    div("a")
  }
}