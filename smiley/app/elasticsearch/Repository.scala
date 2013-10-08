package elasticsearch

import org.joda.time.LocalDate
import play.api.libs.ws._
import java.util.UUID
import play.api.libs.json.Json
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import com.ning.http.client.Realm
import play.Play


object Repository {

  def getSmileys(since: LocalDate, until: Option[LocalDate] = Option.empty) : Map[String, Map[String, String]] = {
    println(since)
    Map("Shaf" -> Map(
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
  }

  def recordHappiness(user: String, date: String, mood: String) : Boolean = {
    val newId = UUID.randomUUID()
    val data = Json.obj(
      "user" -> user,
      "date" -> date,
      "mood" -> mood
    )

    val config = Play.application().configuration()
    val username = config.getString("es.username")
    val password = config.getString("es.password")
    val baseUrl = config.getString("es.baseUrl")

    val futureResponse = WS.url(baseUrl + newId).withAuth(username, password, Realm.AuthScheme.BASIC).put(data)
    val response = Await.result(futureResponse, Duration(10, scala.concurrent.duration.SECONDS))
    response.status >= 200 && response.status <= 299
  }
}