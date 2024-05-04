package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html._
import models.PreferenceData._
import models.ReadsAndWrites._

import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.window
import slinky.web.html._

@react class OnboardingComponent extends Component {
  type Props = Unit
  case class State(gender: String, year: String, greekPreference: String, religion: String, commitment: String, major: String)

  def initialState: State = State("", "", "", "", "", "")

  implicit val ec = scala.concurrent.ExecutionContext.global
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
  val preferenceRoute = document.getElementById("preferenceRoute").asInstanceOf[html.Input].value

  def submitPreferences(): Unit = {
    val username = "kevin" // You will need to replace this with actual logic to obtain the user's username, not sure how to do it yet

    val data = models.PreferenceData(
        username,
        state.gender,
        state.year,
        state.greekPreference,
        state.religion,
        state.commitment,
        state.major
    )

    FetchJson.fetchPost(preferenceRoute, csrfToken, data, (bool: Boolean) => {
        if (bool) {
            println("Preference saved")
            window.alert("Preferences saved successfully!")
        } else {
            window.alert("Failed to save preferences")
        }
    })
    }

  def render(): ReactElement = {
    div(id := "onboarding-section",
      h1("Onboarding"),
      br(),
      span("Gender: "),
      input(`type` := "text", value := state.gender, onChange := (e => setState(state.copy(gender = e.target.value)))),
      br(),
      span("Year: "),
      input(`type` := "text", value := state.year, onChange := (e => setState(state.copy(year = e.target.value)))),
      br(),
      span("Greek Life Preference: "),
      input(`type` := "text", value := state.greekPreference, onChange := (e => setState(state.copy(greekPreference = e.target.value)))),
      br(),
      span("Religion: "),
      input(`type` := "text", value := state.religion, onChange := (e => setState(state.copy(religion = e.target.value)))),
      br(),
      span("Commitment: "),
      input(`type` := "text", value := state.commitment, onChange := (e => setState(state.copy(commitment = e.target.value)))),
      br(),
      span("Major: "),
      input(`type` := "text", value := state.major, onChange := (e => setState(state.copy(major = e.target.value)))),
      br(),
      button("Submit", onClick := (_ => submitPreferences()))
    )
  }
}
