package models

import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.PostgresProfile.api._
import Tables._

class OnboardingModel(db: Database)(implicit ec: ExecutionContext) {
  def createPreference(preferenceData: PreferenceData, username: String): Future[Boolean] = {
    val action = (Preference += PreferenceRow(0, username, Option(preferenceData.gender), Option(preferenceData.year),
      Option(preferenceData.greekPreference), Option(preferenceData.religion),
      Option(preferenceData.commitment), Option(preferenceData.major)))
    db.run(action).map(_ > 0)
  }


  def createProfile(pd: ProfileData, username: String): Future[Boolean] = {
    val action = (Profile += ProfileRow(0, username, pd.firstName, pd.lastName, pd.bio, None, pd.gender, pd.year, 
    pd.greekAssociation, pd.religion, pd.commitment, pd.major))
    db.run(action).map(_ > 0)
  }

  def uploadPhoto(byteArray: Array[Byte], username: String): Future[Boolean] = {
    val photo = for { prof <- Profile if prof.username === username } yield prof.photo
    val updateAction = photo.update(Option(byteArray))
    db.run(updateAction).map(_ > 0)
  }
}
