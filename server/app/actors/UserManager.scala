package actors

import akka.actor.Actor
import akka.actor.ActorRef

class UserManager extends Actor { 
  //lists of actor ref and associated username
  private var userRefs = List.empty[(ActorRef, String)] 

  
  import UserManager._
  def receive: Receive = {
    case RemoveUser(username) => 
    case recipient: String => {userRefs.find(_._2 == recipient) match {
      case Some((recipientRef, _)) => recipientRef ! UserActor.UpdateChat
      case None => //If recipient is not connected to socket don
    }}
    case NewUser(u) =>  {
      if (!userRefs.contains(u)) {
          userRefs ::= u
      }
    }
    case m => println("Unhandled method in UserManager: " + m)
  }
}

object UserManager {
  case class NewUser(user: (ActorRef, String)) 
  case class RemoveUser(user: String)
}