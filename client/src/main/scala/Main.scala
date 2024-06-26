package playscala

import org.scalajs.dom
import shared.SharedMessages
import org.scalajs.dom
import org.scalajs.dom.html

import slinky.web.ReactDOM
import slinky.web.html._

object Main {

  def main(args: Array[String]): Unit = {

    // if (dom.document.getElementById("chatPage") != null) {
    //   println("Rendering chat page")
    //   ReactDOM.render(
    //     ChatPageComponent(),
    //     dom.document.getElementById("react-root")
    //   )
    // }

    if(dom.document.getElementById("authentication-page") != null){
      println("Rendering authentication page")
      ReactDOM.render(
        AuthenticationComponent(),
        dom.document.getElementById("react-root")
      )
    }

    if (dom.document.getElementById("onboarding-page") != null) {
      println("Rendering onboarding page")
      ReactDOM.render(
        OnboardingComponent(),
        dom.document.getElementById("react-root")
      )
    }

   if (dom.document.getElementById("home-page") != null) {
      println("Rendering home page")
      ReactDOM.render(
        HomePageComponent(),
        dom.document.getElementById("react-root")
      )
    }
  }
}
