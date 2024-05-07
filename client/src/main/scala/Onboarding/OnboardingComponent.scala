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
import org.scalajs.dom.Blob
import scala.annotation.switch
import org.scalajs.dom.File
import org.scalajs.dom

// Main Onboarding component that switches between profile component and preference component
@react class OnboardingComponent extends Component {
  type Props = Unit
  case class State(currentComponent: String)
  def initialState: State = State("profile")

  def switchComponent(): Unit = {
    setState(_.copy(currentComponent = "preference"))
  }

  def render(): ReactElement = {
    if (state.currentComponent == "profile"){
      ProfileComponent(switchComponent _)
    } else {
      PreferenceComponent()
    }
  }
}

@react class ProfileComponent extends Component {
  case class Props(switchOnboarding: () => Unit)

  case class State( firstName: String, lastName: String, bio: String, photo: File, gender: String, 
    year: String, greek_association: String, religion: String, commitment: String, major: String
  )

  def initialState: State = State("", "", "", null, "", "", "", "", "", "")

  val profileRoute = document.getElementById("profileRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
  val uploadPhoto = document.getElementById("uploadPhoto").asInstanceOf[html.Input].value
  implicit val ec = scala.concurrent.ExecutionContext.global

  def submitProfile() : Unit = {
    if (state.firstName.isEmpty() || state.lastName.isEmpty()) {
      window.alert("fill out first and last name before submitting")
    } else {
      val data = models.ProfileData( state.firstName, state.lastName, state.bio, state.gender, 
      state.year, state.greek_association, state.religion, state.commitment, state.major)

      FetchJson.fetchPost(profileRoute, csrfToken, data, (bool: Boolean) => {
        if (bool) {
          println("Profile saved")
          props.switchOnboarding()
        } else {
          window.alert("Failed to save preferences")
        }
      })

      val xhr = new dom.XMLHttpRequest()
      xhr.open("POST", uploadPhoto)
      xhr.setRequestHeader("Csrf-Token", csrfToken)
      // xhr.onload = { (e: dom.Event) => 
      //     success(xhr.response)}
      xhr.send(state.photo)

    }
  }

  def render(): ReactElement = {
    div(id := "onboarding-section",
      h1("Your profile"),
      br(),
      span("First Name: "),
      input(`type` := "text", value := state.firstName, onChange := (e => setState(state.copy(firstName = e.target.value)))),
      br(),
      span("Last Name: "),
      input(`type` := "text", value := state.lastName, onChange := (e => setState(state.copy(lastName = e.target.value)))),
      br(),
      span("Bio: "),
      input(`type` := "text", value := state.bio, onChange := (e => setState(state.copy(bio = e.target.value)))),
      br(),
      span("Photo: "),
      input(`type` := "file", onChange := (e => setState(state.copy(photo = e.target.files(0))))),
      br(),
      span("Gender: "),
      input(`type` := "text", value := state.gender, onChange := (e => setState(state.copy(gender = e.target.value)))),
      br(),
      span("Year: "),
      input(`type` := "text", value := state.year, onChange := (e => setState(state.copy(year = e.target.value)))),
      br(),
      span("Greek Life Participation: "),
      input(`type` := "text", value := state.greek_association, onChange := (e => setState(state.copy(greek_association = e.target.value)))),
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
      button("Submit", onClick := (_ => submitProfile()))
    )
  }
}

@react class PreferenceComponent extends Component {
  type Props = Unit
  case class State(gender: String, year: String, greekPreference: String, religion: String, commitment: String, major: String
  )

  def initialState: State = State("", "", "", "", "", "")

  implicit val ec = scala.concurrent.ExecutionContext.global
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
  val preferenceRoute = document.getElementById("preferenceRoute").asInstanceOf[html.Input].value
  val homePageRoute = document.getElementById("homePageRoute").asInstanceOf[html.Input].value

  def submitPreferences(): Unit = { 
    val data = models.PreferenceData(
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
          window.location.assign(homePageRoute)
      } else {
          window.alert("Failed to save preferences")
      }
    })
  }


  def render(): ReactElement = {
    div(id := "onboarding-section",
      h1("Your preferences"),
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

