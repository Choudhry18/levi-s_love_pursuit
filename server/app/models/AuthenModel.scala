package models

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future 

class AuthenModel(db: Database)(implicit ec: ExecutionContext) {
    def validateUser(username: String, password: String): Future[Boolean] = {
      //TODO: encrypt password
      val matches = db.run(Users.filter(userRow => userRow.username === username && userRow.password === password).result)
      matches.map(userRows => 
        if (userRows.isEmpty) false else {
          userRows.headOption.map(_ => true).getOrElse(false)
      })
    }

    def createUser(username: String, password: String, email: String): Future[Boolean] = {
      val matches = db.run(Users.filter(userRow => userRow.username === username).result)
      matches.flatMap { userRows =>
        if (userRows.isEmpty) {
          db.run(Users += UsersRow(username, password, email))
            //add count is probably just to check if adding succeeded
            .flatMap { addCount => 
              if (addCount > 0) db.run(Users.filter(userRow => userRow.username === username).result)
                .map(_.headOption.map(_ => true).getOrElse(false))
              else Future.successful(false)
            }
        } else Future.successful(false)
      }
    }
}