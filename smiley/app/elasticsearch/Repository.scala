package elasticsearch

import org.joda.time.LocalDate
import play.api.Play.current
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Repository(username: String, password: String, baseUrl: String) {

  def getSmileys(since: LocalDate) : Map[String, Map[String, String]] = {
    val data = Json.obj(
      "size" -> 1000,
      "query" -> Json.obj("range" -> Json.obj("date" -> Json.obj("gte" -> since.toString, "lte" -> LocalDate.now().toString)))
    )
    val futureResponse = WS.url(baseUrl + "_search").withAuth(username, password, WSAuthScheme.BASIC).post(data)
    val response = Await.result(futureResponse, Duration(10, scala.concurrent.duration.SECONDS))
    val json = Json.parse(response.body)
    val hits = (json \ "hits" \ "hits").as[Seq[JsValue]]
    val sentiments = hits.map { jsonItem => Map("user" -> (jsonItem \ "_source" \ "user").as[String],
                                                "date" -> (jsonItem \ "_source" \ "date").as[String],
                                                "mood" -> (jsonItem \ "_source" \ "mood").as[String]) }

    sentiments.groupBy(item => item("user"))
      .mapValues(dateGroup => dateGroup.groupBy(item => item("date"))
                   .mapValues(userGroups => userGroups.head("mood")))
  }

  def getSmileysFor(users: List[String], since: LocalDate) : Map[String, Map[String, String]] = {
    getSmileys(since).filter(nameAndSentiment => users.contains(nameAndSentiment._1))
  }

  def recordHappiness(user: String, date: String, mood: String) : Boolean = {
    val newId = user + "-" + date
    val data = Json.obj(
      "user" -> user,
      "date" -> date,
      "mood" -> mood
    )

    val futureResponse = WS.url(baseUrl + newId).withAuth(username, password, WSAuthScheme.BASIC).put(data)
    val response = Await.result(futureResponse, Duration(10, scala.concurrent.duration.SECONDS))
    response.status >= 200 && response.status <= 299
  }
}