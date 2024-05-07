package playscala

import slinky.core.annotations.react
import slinky.core.Component
import slinky.core.facade.ReactElement
import slinky.web.html._
import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom.raw.Blob
import org.scalajs.dom.raw.URL
import scala.scalajs.js
import scalajs.js.typedarray.AB2TA
import _root_.playscala.FetchJson.fetchPost
import models.{ProfileData, PreferenceData}
import models.ReadsAndWrites._

@react class ProfilePageComponent extends Component {
  type Props = Unit
  case class State(
    url: String,
    profile: Option[ProfileData],
    preferences: Option[PreferenceData]
  )

  implicit val ec = scala.concurrent.ExecutionContext.global

  def initialState: State = State("", None, None)

  val getPhotoRoute = document.getElementById("getPhotoRoute").asInstanceOf[html.Input].value
  val getProfileRoute = document.getElementById("getProfileRoute").asInstanceOf[html.Input].value
  val getPreferencesRoute = document.getElementById("getPreferencesRoute").asInstanceOf[html.Input].value
  val csrfToken = document.getElementById("csrfToken").asInstanceOf[html.Input].value

  override def componentDidMount(): Unit = {
    fetchPhoto()
    fetchProfile()
    fetchPreferences()
  }

  def fetchPhoto(): Unit = {
  FetchJson.fetchGet(getPhotoRoute, (bArray: Array[Byte]) => {
    if (bArray.isEmpty) {
      setState(state.copy(url = "/assets/images/favicon.png"))
    } else {
      val blob = new Blob(js.Array(bArray.toTypedArray))
      val imgUrl = URL.createObjectURL(blob)
      setState(state.copy(url = imgUrl))
    }
  })
}

  def fetchProfile(): Unit = {
    FetchJson.fetchGet(getProfileRoute, (profileData: ProfileData) => {
      setState(state.copy(profile = Some(profileData)))
    })
  }

  def fetchPreferences(): Unit = {
    FetchJson.fetchGet(getPreferencesRoute, (preferenceData: PreferenceData) => {
      setState(state.copy(preferences = Some(preferenceData)))
    })
  }

  def render(): ReactElement = {
  div(
    div(id := "image-container")(
      img(src := state.url, id := "profile-image")
    ),
    div(id := "content-container")(
      state.profile.map { profile =>
        div(id := "profile-info", className := "info-box")(
          h2("Profile Information"),
          p(s"Username: ${profile.username}"),
          p(s"First Name: ${profile.firstName}"),
          p(s"Last Name: ${profile.lastName}"),
          p(s"Bio: ${profile.bio.getOrElse("")}"),
          p(s"Gender: ${profile.gender.getOrElse("")}"),
          p(s"Year: ${profile.year.getOrElse("")}"),
          p(s"Greek Association: ${profile.greekAssociation.getOrElse("")}"),
          p(s"Religion: ${profile.religion.getOrElse("")}"),
          p(s"Commitment: ${profile.commitment.getOrElse("")}"),
          p(s"Major: ${profile.major.getOrElse("")}")
        )
      },
      state.preferences.map { preferences =>
        div(id := "preferences-info", className := "info-box")(
          h2("Preference Information"),
          p(s"Preferred Gender: ${preferences.gender}"),
          p(s"Preferred Year: ${preferences.year}"),
          p(s"Preferred Greek Preference: ${preferences.greekPreference}"),
          p(s"Preferred Religion: ${preferences.religion}"),
          p(s"Preferred Commitment: ${preferences.commitment}"),
          p(s"Preferred Major: ${preferences.major}")
        )
      }
    )
    )
  }
}