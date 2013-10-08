package elasticsearch

import org.joda.time.LocalDate
import play.api.libs.ws._
import java.util.UUID
import play.api.libs.json.Json
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import com.ning.http.client.Realm
import play.api.libs.json.JsValue


class Repository(username: String, password: String, baseUrl: String) {

  def getSmileys(since: LocalDate) : Map[String, Map[String, String]] = {
    val data = Json.obj(
      "query" -> Json.obj("range" -> Json.obj("date" -> Json.obj("gte" -> since.toString, "lte" -> LocalDate.now().toString)))
    )
    val futureResponse = WS.url(baseUrl + "_search").withAuth(username, password, Realm.AuthScheme.BASIC).post(data)
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

  def recordHappiness(user: String, date: String, mood: String) : Boolean = {
    val newId = UUID.randomUUID()
    val data = Json.obj(
      "user" -> user,
      "date" -> date,
      "mood" -> mood
    )
    
    val futureResponse = WS.url(baseUrl + newId).withAuth(username, password, Realm.AuthScheme.BASIC).put(data)
    val response = Await.result(futureResponse, Duration(10, scala.concurrent.duration.SECONDS))
    response.status >= 200 && response.status <= 299
  }
}