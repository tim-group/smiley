package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson
import elasticsearch.Repository
import org.joda.time.LocalDate


object Smilies extends Controller {

  def from(fromDate: String) = Action {

    val data = Repository.getSmileys(LocalDate.parse(fromDate))

    Ok(toJson(data))
  }

  def recordHappiness = Action { request =>
    val result = for {
      user <- request.getQueryString("user")
      date <- request.getQueryString("date")
      mood <- request.getQueryString("mood")
    } yield Repository.recordHappiness(user, date, mood)
    result match {
      case Some(true) => Ok("Hello")
      case _ => BadRequest("Went wrong")
    }

  }
}
