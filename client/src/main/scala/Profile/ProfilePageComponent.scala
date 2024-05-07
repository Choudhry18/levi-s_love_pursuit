package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html.div
import org.scalajs.dom.document
import org.scalajs.dom.html
import _root_.playscala.FetchJson.fetchPost

import org.scalajs.dom.raw.Blob
import org.scalajs.dom.raw.URL
import scala.scalajs.js
import scalajs.js.typedarray.AB2TA
import slinky.web.html._

@react class ProfilePageComponent extends Component {
  type Props = Unit
  case class State(url: String)
  implicit val ec = scala.concurrent.ExecutionContext.global
  def initialState: State = State("")
  val getPhotoRoute = document.getElementById("getPhotoRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value

  override def componentDidMount(): Unit = {
    FetchJson.fetchGet(getPhotoRoute, (bArray: Array[Byte]) => {

      val blob = new Blob(js.Array(bArray.toTypedArray))
      println(bArray.length)
      val imgUrl = URL.createObjectURL(blob)
      setState(state.copy(url = imgUrl))
    }
    )
  }

  def render(): ReactElement = {
    img(src := state.url)
  }
}