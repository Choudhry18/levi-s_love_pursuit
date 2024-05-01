package playscala

import models.UserData
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html
import org.scalajs.dom._
import play.api.libs.json.Json
import scala.scalajs.js.Thenable.Implicits._
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import scala.concurrent.ExecutionContext
import scala.scalajs.js.annotation.JSExportTopLevel
import scalajs.js
import slinky.web.html.method
import slinky.web.svg.mode
import slinky.web.html.body
import models.ReadsAndWrites
import play.api.libs.json.Writes
import play.api.libs.json.Reads

object FetchJson {
  def fetchPost[A, B](url: String, csrfToken: String, data: A, success: B => Unit)
  (implicit writes: Writes[A], reads: Reads[B], ec: ExecutionContext): Unit = {
    val oheaders = new Headers()
    oheaders.set("Content-Type", "application/json")
    oheaders.set("Csrf-Token", csrfToken)
    val ri = new dom.RequestInit() {
      this.method = HttpMethod.POST
      mode = RequestMode.cors 
      headers = oheaders
      body = Json.toJson(data).toString
    }

    Fetch.fetch(url, ri)
      .flatMap(res => res.text()).map { data => 
        Json.fromJson[B](Json.parse(data)) match {
          case JsSuccess(b, path) =>
            success(b)
          case e @ JsError(_) =>
            println("Fetch error: " + e)
        }
    }
  }

  def fetchGet[B](url: String, success: B => Unit)(implicit
      reads: Reads[B], ec: ExecutionContext): Unit = {
    Fetch.fetch(url)
      .flatMap(res => res.text())
      .map { data => 
        Json.fromJson[B](Json.parse(data)) match {
          case JsSuccess(b, path) =>
            success(b)
          case e @ JsError(_) =>
            println("Fetch error: " + e)
        }
    }
  }
}