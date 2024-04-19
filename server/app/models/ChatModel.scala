package models

import collection.mutable

object ChatModel {
    private val chats = mutable.Map[String, Seq[String]]("Levi" -> Seq("Choudhry", "Kevin", "Harry", "Patrick"))
    private val chatContents = mutable.Map[Set[String], Seq[(String, String)]]((Set("Levi", "Choudhry") -> Seq(("Levi", "Hi Choudhry"), ("Choudhry", "Hi Levi"), ("Choudhry", "You are so smart"), ("Levi", "You are a literal goomba"))), 
        (Set("Levi", "Kevin") -> Seq(("Levi", "Hi Kevin"), ("Kevin", "Hi Levi"))),
        (Set("Levi", "Harry") -> Seq(("Levi", "Hi Harry"), ("Harry", "Hi Levi"))),
        (Set("Levi", "Patrick") -> Seq(("Levi", "Hi Patrick"), ("Patrick", "Hi Levi"))))

    def getChats(username: String): Seq[String] = {
        chats.get(username).getOrElse(Nil)
    }

    def getChatContent(user1: String, user2: String) : Seq[(String, String)] = {
        //method actually works regardless of order of user
        chatContents.get(Set(user1, user2)).getOrElse(Nil)
    }

    def addMessage(sender: String, recipient: String, message: String) : Unit = {
        val userPair = Set(sender, recipient)
        //if there is existing chat already, add that on to the list, else create a new mapping
        chatContents.get(userPair) match {
            case Some(chatContent) => chatContents.put(userPair, chatContent :+ (sender, message))
            case None => chatContents.put(userPair, Seq((sender, message)))
        }
    }
}