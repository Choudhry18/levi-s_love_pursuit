package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json._

class UserActor(out : ActorRef, username: String, manager: ActorRef) extends Actor { 
  manager ! UserManager.NewUser((self, username))
  import UserActor._
  def receive: Receive = {
    case recipient : String => manager ! recipient
    case close : Int => manager ! UserManager.RemoveUser(username)
    case UpdateChat() => out ! Json.stringify(Json.toJson("update chat"))
    case m => println("Unhandled method in MessageActor: " + m)
  }
}
object UserActor {
    def props(out: ActorRef, username: String, manager: ActorRef) = Props(new UserActor(out, username, manager)) 
    case class UpdateChat()
}