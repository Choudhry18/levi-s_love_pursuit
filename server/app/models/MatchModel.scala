package models

import collection.mutable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future 


class MatchModel(db: Database)(implicit ec: ExecutionContext) {
  def getProfiles(username: String): Future[Seq[models.ProfileData]] = {
    val query = for {
      (user, profile) <- Users joinLeft Profile on (_.username === _.username)
      if user.username =!= username
    } yield profile
    
    db.run(query.result).map(_.flatten.map(rowToProfileData))
  }

  private def rowToProfileData(row: models.Tables.ProfileRow): models.ProfileData = {
    models.ProfileData(
      row.username,
      row.firstName,
      row.lastName,
      row.bio,
      row.photo,
      row.gender,
      row.year,
      row.greekAssociation,
      row.religion,
      row.commitment,
      row.major
    )
  }

  def sendSwipe(swiper: String, swipee: String): Future[Boolean] = {

    val query = Swipe.filter(swipeRow => (swipeRow.swiper === swipee && swipeRow.swipee === swiper))
    db.run(query.result).flatMap{matchRows =>
      if (matchRows.nonEmpty) {
        val insertMatch = Match += MatchRow(-1, swiper, swipee)
        db.run(insertMatch).map(_ > 0)
      } else {
        val insertSwipe = Swipe += SwipeRow(-1, swiper, swipee)
        db.run(insertSwipe).map(_ < 0)
      }
    }
  }

}