package models

import collection.mutable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future 

class ChatModel(db: Database)(implicit ec: ExecutionContext) {
    private val chatContents = mutable.Map[Set[String], Seq[UserMessage]]((Set("levi", "Choudhry") -> Seq(UserMessage("levi", "Hi Choudhry"), UserMessage("Choudhry", "Hi levi"), UserMessage("Choudhry", "You are so smart"), UserMessage("levi", "You are a literal goomba"))), 
      (Set("levi", "kevin") -> Seq(UserMessage("levi", "Hi kevin"), UserMessage("kevin", "Hi levi"))),
      (Set("levi", "Harry") -> Seq(UserMessage("levi", "Hi Harry"), UserMessage("Harry", "Hi levi"))),
      (Set("levi", "Patrick") -> Seq(UserMessage("levi", "Hi Patrick"), UserMessage("Patrick", "Hi levi"))))

    //return a seq of username that a user is chatting with
    def getChats(userId: Int): Future[Seq[String]] = {
      val dbMatches : Future[Seq[MatchRow]] = db.run(Match.filter(matchRow => matchRow.userId1 === userId || matchRow.userId2 === userId).result)
      dbMatches.flatMap(matchRows => 
        //turn a sequence of futures into a future sequence
        Future.sequence(
          // Seq(Future.successful(""))
          matchRows.map(matchRow => {
              val otherUserId = if (matchRow.userId1.getOrElse(0) != userId) matchRow.userId1 else matchRow.userId2
              db.run(Users.filter(userRow => userRow.userId === otherUserId).result).map(_.headOption.map(_.username).getOrElse(""))
            }
          )
        )
      )
    }

    def getChatContent(user1: String, user2: String) : Seq[UserMessage] = {
        //method actually works regardless of order of user
        chatContents.get(Set(user1, user2)).getOrElse(Nil)
    }

    def addMessage(sender: String, recipient: String, message: String) : Unit = {
        val userPair = Set(sender, recipient)
        //if there is existing chat already, add that on to the list, else create a new mapping
        chatContents.get(userPair) match {
            case Some(chatContent) => chatContents.put(userPair, chatContent :+ UserMessage(sender, message))
            case None => chatContents.put(userPair, Seq(UserMessage(sender, message)))
        }
    }
}