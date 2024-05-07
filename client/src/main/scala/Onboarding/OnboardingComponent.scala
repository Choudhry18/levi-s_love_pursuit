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
import models.ProfileData
import models.UserMessage
import models.UserData

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

  case class State( email: String, username: String, password: String, firstName: String, lastName: String, bio: String, photo: File, gender: String, 
    year: String, greek_association: String, religion: String, commitment: String, major: String
  )

  def initialState: State = State("","","","", "", "", null, "", "", "", "", "", "")

  val profileRoute = document.getElementById("profileRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value
  val uploadPhoto = document.getElementById("uploadPhoto").asInstanceOf[html.Input].value
  val createRoute = document.getElementById("createRoute").asInstanceOf[html.Input].value
  
  implicit val ec = scala.concurrent.ExecutionContext.global

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    println(state.year)
  }

  def submitProfile() : Unit = {
    if (state.firstName.isEmpty() || state.lastName.isEmpty() || state.email.isEmpty() || state.username.isEmpty() || state.password.isEmpty()) {
      window.alert("fill out the red boxes")
      val requiredFields = document.getElementsByClassName("required")
      requiredFields.foreach(element => 
        element.setAttribute("style", "border-color:red;")
      )
    } else {
      val data = ProfileData( state.username, state.firstName, state.lastName, Option(state.bio), None, // photoUrl is not provided in State 
      Option(state.gender), Option(state.year), Option(state.greek_association), Option(state.religion), Option(state.commitment), Option(state.major)
      )

      FetchJson.fetchPost(createRoute, csrfToken, UserData(state.email, state.username, state.password), (success: Boolean) => {
        if (!success) {
          window.alert("User or email already exists")
        } else {
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
          val formData = new dom.FormData()
          formData.append("fieldName",state.photo)
          xhr.send(formData)
        }
      })
    }
  }

  val years = List("Rather not share", "Freshman", "Sophomore", "Junior", "Senior")
  val greeks = List("Rather not share", "Bengal Lancers", "Chi Delta Tau", "Iota Chi Rho", "Kappa Kappa Delta", 
  "Omega Phi", "Phi Sigma Chi", "Triniteers", "Alpha Chi Lambda", "Chi Beta Epsilon", 
  "Delta Theta Nu", "Gamma Chi Delta", "Phi Delta Kappa", "Sigma Theta Tau", "Spurs Sorority", "Zeta Chi")
  val commitments = List("Rather not share", "Just here to have fun", "Figuring it out", "Long-term relationship")

  def render(): ReactElement = {
    div(id := "onboarding-section",
      h1("Your profile"),
      br(),
      span("Email: "),
      input(`type` := "text", className := "required", value := state.email, onChange := (e => setState(state.copy(email = e.target.value)))),
      br(),
      span("Username: "),
      input(`type` := "text", className := "required", value := state.username, onChange := (e => setState(state.copy(username = e.target.value)))),
      br(),
      span("Password: "),
      input(`type` := "password", className := "required", value := state.password, onChange := (e => setState(state.copy(password = e.target.value)))),
      br(),
      span("First Name: "),
      input(`type` := "text", className := "required", value := state.firstName, onChange := (e => setState(state.copy(firstName = e.target.value)))),
      br(),
      span("Last Name: "),
      input(`type` := "text", className := "required", value := state.lastName, onChange := (e => setState(state.copy(lastName = e.target.value)))),
      br(),
      span("Bio: "),
      input(`type` := "text", id := "bioInput", value := state.bio, onChange := (e => setState(state.copy(bio = e.target.value)))),
      br(),
      span("Photo: "),
      input(`type` := "file", onChange := (e => setState(state.copy(photo = e.target.files(0))))),
      br(),
      br(),
      span("Gender: "),
      input(`type` := "text", value := state.gender, onChange := (e => setState(state.copy(gender = e.target.value)))),
      br(),
      span("Year: "),
      select(value := state.year, onChange := (e => setState(state.copy(year = e.target.value))))(
        years.map(yr => option(value := {if (yr == "Rather not share") "" else yr}, key := yr)(yr))
      ),
      br(),
      span("Greek Life Participation: "),
      select(value := state.greek_association, onChange := (e => setState(state.copy(greek_association = e.target.value))))(
        greeks.map(greek => option(value := {if (greek == "Rather not share") "" else greek}, key := greek)(greek))
      ),
      br(),
      span("Commitment: "),
      select(value := state.commitment, onChange := (e => setState(state.copy(commitment = e.target.value))))(
        commitments.map(com => option(value := {if (com == "Rather not share") "" else com}, key := com)(com))
      ),
      br(),
      span("Religion: "),
      input(`type` := "text", value := state.religion, onChange := (e => setState(state.copy(religion = e.target.value)))),
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

