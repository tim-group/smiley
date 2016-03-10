package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json.toJson
import elasticsearch.Repository
import org.joda.time.LocalDate
import play.Play

object Smilies extends Controller {

  def forUsers(fromDate: String) = Action { request =>
    val result = for {
      users <- request.queryString.get("user")
    } yield users
    result match {
      case Some(users) => Ok(toJson(repository().getSmileysFor(users.toList, LocalDate.parse(fromDate))))
      case _ =>  Ok(toJson(repository().getSmileys(LocalDate.parse(fromDate))))
    }
  }

  def weeklyAverages(fromDate: String) = Action { request =>
    val result = for {
      users <- request.queryString.get("user")
    } yield users
    result match {
      case Some(users) => Ok(toJson(convertToAverages(repository().getSmileysFor(users.toList, LocalDate.parse(fromDate)))))
      case _ =>  Ok(toJson(convertToAverages(repository().getSmileys(LocalDate.parse(fromDate)))))
    }
  }

   def convertToAverages(sentimentData: Map[String, Map[String, String]]) :  Map[String, Map[String, String]] = {
       sentimentData.mapValues(averageOnePersonsSentiment(_))
   }
   
   def averageOnePersonsSentiment(onePersonsData: Map[String, String]): Map[String, String] = {
     val byWeek: Map[String, Map[String, String]] = onePersonsData.groupBy(dayAndSentiment => LocalDate.parse(dayAndSentiment._1).getWeekOfWeekyear.toString)
     byWeek.filter(_._2.size > 2)
     	   .mapValues(sentiments => averageOf(sentiments))
   }
   
   def averageOf(sentiments: Map[String, String]): String = {
     (sentiments.map(dayAndSentiment => validMoods.indexOf(dayAndSentiment._2) - 1).foldLeft(0)(_ + _) / sentiments.size.toFloat).toString
   } 
        
  def validMoods = List( "sad", "neutral", "happy" )

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
    val username = System.getProperty("es.username")
    val password = System.getProperty("es.password")
    val baseUrl = System.getProperty("es.baseUrl")
    new Repository(username, password, baseUrl)
  }
}

