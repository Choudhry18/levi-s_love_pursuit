package playscala

import org.scalajs.dom
import shared.SharedMessages
import org.scalajs.dom
import org.scalajs.dom.html
import Authentication.Version6

import slinky.web.ReactDOM
import slinky.web.html._

object Main {

  def main(args: Array[String]): Unit = {

    // What is below is using Scala.js with Slinky to use React.
    if (dom.document.getElementById("chatPage") != null) {
      println("Rendering chat page")
      ReactDOM.render(
        ChatPageComponent(),
        dom.document.getElementById("react-root")
      )
    }

    if(dom.document.getElementById("version6") != null){
        Version6.init()
    }
  }
}
