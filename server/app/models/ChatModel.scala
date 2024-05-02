package models

import collection.mutable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future 
import java.sql.Timestamp
import java.util.Date

class ChatModel(db: Database)(implicit ec: ExecutionContext) {
    //return a seq of username that a user is chatting with
    def getChats(username : String): Future[Seq[String]] = {
      val matchMatches : Future[Seq[MatchRow]] = db.run(Match.filter(matchRow => matchRow.username1 === username || matchRow.username2 === username).result)
      matchMatches.flatMap(matchRows => 
        //turn a sequence of futures into a future sequence
        Future.sequence(
          matchRows.map(matchRow => {
            val otherUsername = if (matchRow.username1 != username) matchRow.username1 else matchRow.username2
            db.run(Users.filter(userRow => userRow.username === otherUsername).result).map(_.headOption.map(_.username).getOrElse(""))
          })
        )
      )
    }

    def getChatContent(user1: String, user2: String) : Future[Seq[UserMessage]] = {
      //find the match
      val matchMatches : Future[Seq[MatchRow]] = db.run(Match.filter
        (matchRow => (matchRow.username1 === user1 || matchRow.username1 === user2) 
        && (matchRow.username2 === user1 || matchRow.username2 === user2)).result)
      matchMatches.flatMap(matchRows => 
        matchRows.headOption.map(matchRow => {
          val messageMatches : Future[Seq[MessageRow]] = 
            db.run(Message.filter(messageRow => messageRow.matchId === matchRow.matchId).sortBy(messageRow => messageRow.timestamp).result)
          messageMatches.map(messageRows => messageRows.map(messageRow => UserMessage(messageRow.senderUsername, messageRow.messageText)))
        }).getOrElse(Future.successful(Nil))
      )
    }

    def addMessage(sender: String, recipient: String, message: String) : Unit = {
      val matchMatches : Future[Seq[MatchRow]] = db.run(Match.filter
        (matchRow => (matchRow.username1 === sender || matchRow.username1 === recipient) 
        && (matchRow.username2 === sender || matchRow.username2 === recipient)).result)
      matchMatches.flatMap(matchRows => 
        matchRows.headOption.map(matchRow => {
          db.run(Message += MessageRow(-1, matchRow.matchId, sender, message, new Timestamp(new Date().getTime)))
        }).getOrElse(Future.successful(false))
      )
    }
}