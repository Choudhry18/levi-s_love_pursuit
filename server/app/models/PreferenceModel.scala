package models

import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.PostgresProfile.api._
import Tables._

class PreferenceModel(db: Database)(implicit ec: ExecutionContext) {
    def createPreference(preferenceData: PreferenceData, username: String): Future[Boolean] = {
      val action = (Preference += PreferenceRow(0, username, Option(preferenceData.gender), Option(preferenceData.year),
        Option(preferenceData.greekPreference), Option(preferenceData.religion),
        Option(preferenceData.commitment), Option(preferenceData.major)))
      db.run(action).map(_ > 0)
    }
}
