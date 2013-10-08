package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson
import elasticsearch.Repository


object Smilies extends Controller {

  def from(fromDate: String) = Action {

    println(fromDate)
    val data = Map("Shaf" -> Map(
        "2013-09-30" -> "sad",
        "2013-10-01" -> "happy",
        "2013-10-02" -> "neutral",
        "2013-10-03" -> "neutral",
        "2013-10-04" -> "happy",
        "2013-10-08" -> "happy"
        ),
        "Tom W" -> Map(
        "2013-10-01" -> "happy",
        "2013-10-02" -> "neutral",
        "2013-10-03" -> "neutral",
        "2013-10-04" -> "sad",
        "2013-10-08" -> "neutral"
        ),
        "Sergiusz" -> Map(
        "2013-09-30" -> "happy",
        "2013-10-01" -> "happy",
        "2013-10-02" -> "neutral",
        "2013-10-03" -> "neutral",
        "2013-10-04" -> "happy",
        "2013-10-08" -> "neutral"
        )
        )

    Ok(toJson(data))
  }

  def recordHappiness = Action {
    Repository.recordHappiness("markus", "2013-10-08", "happy")
    Ok("Hello")
  }
}