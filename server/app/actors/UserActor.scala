package actors

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import play.api.libs.json._

class UserActor(out : ActorRef, username: String, manager: ActorRef) extends Actor { 
  manager ! UserManager.NewUser((self, username))
  println(username + " accessed socket")
  import UserActor._
  def receive: Receive = {
    case str : String => { str match {
      case "close" => manager ! {
        println("closing " + username + "'s socket")
        UserManager.RemoveUser(username)
      }
      case recipient => manager ! recipient
      }
    }

    case UpdateChat() => out ! Json.stringify(Json.toJson("update chat"))
    case m => println("Unhandled method in UserActor: " + m)
  }
}
object UserActor {
    def props(out: ActorRef, username: String, manager: ActorRef) = Props(new UserActor(out, username, manager)) 
    case class UpdateChat()
}