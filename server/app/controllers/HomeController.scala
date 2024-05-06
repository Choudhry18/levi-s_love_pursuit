package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.actor.Props
import actors._

@Singleton
class HomeController @Inject() (cc: ControllerComponents)(implicit
    system: ActorSystem,
    mat: Materializer
) extends AbstractController(cc) {

  def load = Action { implicit request =>
    Ok(views.html.home())
  }

  val userManager = system.actorOf(Props[UserManager], "UserManager")

  def socket = WebSocket.accept[String, String] { request =>
    val username = request.session.get("username").get
    ActorFlow.actorRef { out => UserActor.props(out, username, userManager)}
  }
}