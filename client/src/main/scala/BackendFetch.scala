package playscala

import play.api.libs.json.JsError
import org.scalajs.dom.experimental.Headers
import org.scalajs.dom.experimental.Fetch
import org.scalajs.dom.experimental.HttpMethod
import org.scalajs.dom.experimental.RequestInit
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.Writes
import play.api.libs.json.Reads
//auto change promises to future and allow .flatMap
import scala.scalajs.js.Thenable.Implicits._
import scala.concurrent.ExecutionContext
import org.scalajs.dom._
import org.scalajs.dom

import scala.scalajs.js


object BackendFetch {
  def fetchPost(url: String, csrfToken: String, data: String, success: js.Any => Unit): Unit = {
    val xhr = new XMLHttpRequest()
    xhr.open("POST", url)
    xhr.setRequestHeader("Csrf-Token", csrfToken)
    xhr.onload = { (e: Event) => 
        success(xhr.response)}
    xhr.send(data)
    //error handling?
  }

  class MyRequestInit(
        method: js.UndefOr[HttpMethod] = js.undefined,
        headers: js.UndefOr[HeadersInit] = js.undefined,
        body: js.UndefOr[BodyInit] = js.undefined,
        referrer: js.UndefOr[String] = js.undefined,
        referrerPolicy: js.UndefOr[ReferrerPolicy] = js.undefined,
        mode: js.UndefOr[RequestMode] = js.undefined,
        credentials: js.UndefOr[RequestCredentials] = js.undefined,
        cache: js.UndefOr[RequestCache] = js.undefined,
        redirect: js.UndefOr[RequestRedirect] = js.undefined,
        integrity: js.UndefOr[String] = js.undefined,
        keepalive: js.UndefOr[Boolean] = js.undefined,
        signal: js.UndefOr[AbortSignal] = js.undefined,
        duplex: js.UndefOr[RequestDuplex] = js.undefined,
        window: js.UndefOr[Null] = js.undefined
    ) extends RequestInit

  def oldFetchPost[A, B](url: String, csrfToken: String, 
      data: A, success: B => Unit, error: JsError => Unit)(implicit
      writes: Writes[A], reads: Reads[B], ec: ExecutionContext): Unit = {
    val headers = new Headers()
    headers.set("Content-Type", "application/json")
    headers.set("Csrf-Token", csrfToken)
    val myRequestInit = new MyRequestInit(
        method = HttpMethod.POST,
        headers = headers,
        body = Json.toJson(data).toString
    )
    Fetch.fetch(url, myRequestInit)
      .flatMap(res => res.text())
      .map { data => 
        //B is whatever we receive from the server
        Json.fromJson[B](Json.parse(data)) match {
          case JsSuccess(b, path) =>
            success(b)
          case e @ JsError(_) =>
            error(e)
        }
    }
  }

  def fetchGet[B](url: String, success: B => Unit, error: JsError => Unit)(implicit
      reads: Reads[B], ec: ExecutionContext): Unit = {
    Fetch.fetch(url)
      .flatMap(res => res.text())
      .map { data => 
        Json.fromJson[B](Json.parse(data)) match {
          case JsSuccess(b, path) =>
            success(b)
          case e @ JsError(_) =>
            error(e)
        }
    }
  }

  //parsing what you get back to scala objects
  def parseChatContent(chatContent : js.Any) : Seq[(String, String)] = {
    var seqChatContent : Seq[(String, String)] = Nil
    val TupleRegex = """\["([^"]+)","([^"]+)"\]""".r
    // Regex.Match
    TupleRegex.findAllMatchIn(chatContent.toString()).foreach { m =>
      m match {
        case TupleRegex(sender, message) => {
          seqChatContent = seqChatContent :+ (sender, message)
        }
      }
    }
    seqChatContent
  }

  //parsing what you get back to a boolean value
  def parseBoolean(boolObject : js.Any) : Boolean = {
    if ( boolObject.toString() == "true") 
      return true
    else
      return false
  }
}