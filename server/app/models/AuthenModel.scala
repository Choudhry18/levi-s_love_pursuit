package models

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future 

class AuthenModel(db: Database)(implicit ec: ExecutionContext) {
    // private val users = mutable.Map[String, String]("kevin" -> "pass", "levi" -> "pass")

    //the int is the associated user ID
    def validateUser(username: String, password: String): Future[Option[Int]] = {
      //TODO: encrypt password
      val matches = db.run(Users.filter(userRow => userRow.username === username && userRow.password === password).result)
      matches.map(userRows => 
        if (userRows.isEmpty) None else {
          userRows.headOption.flatMap {userRow => Some(userRow.userId)
        }
      })
    }

    def createUser(username: String, password: String, email: String): Future[Option[Int]] = {
      val matches = db.run(Users.filter(userRow => userRow.username === username).result)
      matches.flatMap { userRows =>
        if (userRows.isEmpty) {
          db.run(Users += UsersRow(-1, username, password, email))
            //add count is probably just to check if adding succeeded
            .flatMap { addCount => 
              if (addCount > 0) db.run(Users.filter(userRow => userRow.username === username).result)
                .map(_.headOption.map(_.userId))
              else Future.successful(None)
            }
        } else Future.successful(None)
      }
    }
}