package models

import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.PostgresProfile.api._
import Tables._

class ProfileModel(db: Database)(implicit ec: ExecutionContext) {
  def getPhoto(username: String): Future[Array[Byte]] = {
    val matches = db.run(Profile.filter(profileRow => profileRow.username === username).result)
    matches.map(profileRows => profileRows.headOption.flatMap(p => p.photo).getOrElse(null))
  } 

  def getProfile(username: String): Future[Option[ProfileData]] = {
    val query = Profile.filter(_.username === username)
    db.run(query.result.headOption).map(_.map(row =>
      ProfileData(
        username = row.username,
        firstName = row.firstName,
        lastName = row.lastName,
        bio = row.bio,
        photo = row.photo,
        gender = row.gender,
        year = row.year,
        greekAssociation = row.greekAssociation,
        religion = row.religion,
        commitment = row.commitment,
        major = row.major
      )
    ))
  }

  def getPreferences(username: String): Future[Option[PreferenceData]] = {
    val query = Preference.filter(_.username === username)
    db.run(query.result.headOption).map(_.map(row =>
      PreferenceData(
        gender = row.gender.getOrElse(""),
        year = row.year.getOrElse(""),
        greekPreference = row.greekPreference.getOrElse(""),
        religion = row.religion.getOrElse(""),
        commitment = row.commitment.getOrElse(""),
        major = row.major.getOrElse("")
      )
    ))
  }
}


