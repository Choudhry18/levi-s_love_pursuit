package models

import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.PostgresProfile.api._
import Tables._

class ProfileModel(db: Database)(implicit ec: ExecutionContext) {
  def getPhoto(username: String): Future[Array[Byte]] = {
    val matches = db.run(Profile.filter(profileRow => profileRow.username === username).result)
    matches.map(profileRows => profileRows.headOption.flatMap(p => p.photo).getOrElse(null))
  } 
}