package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson


object Smilies extends Controller {

  def lastTwoWeeks = Action {

    val data = Map("shaf" -> Map("2013-10-08" -> "happy"))

    Ok(toJson(data))
  }

}