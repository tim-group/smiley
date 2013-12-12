package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson
import elasticsearch.Repository
import org.joda.time.LocalDate
import play.Play

object Smilies extends Controller {

  // def from(fromDate: String) = Action {
  //   val data = repository().getSmileys(LocalDate.parse(fromDate))
  //   Ok(toJson(data))
  // }

  def forUsers(fromDate: String) = Action { request =>
    val result = for {
      users <- request.queryString.get("user")
    } yield users
    result match {
      case Some(users) => Ok(toJson(repository().getSmileysFor(users.toList, LocalDate.parse(fromDate))))
      case _ =>  Ok(toJson(repository().getSmileys(LocalDate.parse(fromDate))))
    }
  }

  def validMoods = List("happy", "neutral", "sad")

  def valid(mood: Option[String]) : Option[String] = {
    if (validMoods.contains(mood.getOrElse(""))) mood else None
  }

  def recordHappiness = Action { request =>
    val result = for {
      user <- request.getQueryString("user")
      date <- request.getQueryString("date")
      mood <- valid(request.getQueryString("mood"))
    } yield repository().recordHappiness(user, date, mood)

    result match {
      case Some(true) => Ok("Sentiment has been recorded")
      case _ => BadRequest("Could not record sentiment")
    }
  }

  def repository() : Repository = {
    val config = Play.application().configuration()
    val username = config.getString("es.username")
    val password = config.getString("es.password")
    val baseUrl = config.getString("es.baseUrl")
    new Repository(username, password, baseUrl)
  }
}

