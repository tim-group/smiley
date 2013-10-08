package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson


object Smilies extends Controller {

  def lastTwoWeeks = Action {

    val data = Map("Shaf" -> Map(
        "2013-09-30" -> "unhappy",
        "2013-10-01" -> "happy",
        "2013-10-02" -> "meh",
        "2013-10-03" -> "meh",
        "2013-10-04" -> "happy",
        "2013-10-07" -> "happy",
        "2013-10-08" -> "happy"
        ),
        "Tom W" -> Map(
        "2013-10-01" -> "happy",
        "2013-10-02" -> "meh",
        "2013-10-03" -> "meh",
        "2013-10-04" -> "unhappy",
        "2013-10-07" -> "meh"
        ),
        "Sergiusz" -> Map(
        "2013-09-30" -> "happy",
        "2013-10-01" -> "happy",
        "2013-10-02" -> "meh",
        "2013-10-03" -> "meh",
        "2013-10-04" -> "happy",
        "2013-10-08" -> "meh"
        )
        )

    Ok(toJson(data))
  }

}