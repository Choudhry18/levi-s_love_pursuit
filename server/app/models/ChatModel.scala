package models

import collection.mutable

object ChatModel {
    private val chats = mutable.Map[String, Seq[String]]("Levi" -> Seq("Choudhry", "Kevin", "Harry", "Patrick"), "Kevin" -> Seq("Levi", "Choudhry"))
    private val chatContents = mutable.Map[Set[String], Seq[UserMessage]]((Set("Levi", "Choudhry") -> Seq(UserMessage("Levi", "Hi Choudhry"), UserMessage("Choudhry", "Hi Levi"), UserMessage("Choudhry", "You are so smart"), UserMessage("Levi", "You are a literal goomba"))), 
        (Set("Levi", "Kevin") -> Seq(UserMessage("Levi", "Hi Kevin"), UserMessage("Kevin", "Hi Levi"))),
        (Set("Levi", "Harry") -> Seq(UserMessage("Levi", "Hi Harry"), UserMessage("Harry", "Hi Levi"))),
        (Set("Levi", "Patrick") -> Seq(UserMessage("Levi", "Hi Patrick"), UserMessage("Patrick", "Hi Levi"))))

    def getChats(username: String): Seq[String] = {
        chats.get(username).getOrElse(Nil)
    }

    def getChatContent(user1: String, user2: String) : Seq[UserMessage] = {
        println(chatContents.get(Set(user1, user2)))
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