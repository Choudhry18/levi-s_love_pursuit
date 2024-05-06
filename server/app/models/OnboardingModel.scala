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
    val action = (Profile += ProfileRow(0, username, pd.firstName, pd.lastName, Option(pd.bio), Option(pd.photo), Option(pd.gender), Option(pd.year), 
    Option(pd.greek_association), Option(pd.religion), Option(pd.commitment), Option(pd.major)))
    db.run(action).map(_ > 0)
  }
}
