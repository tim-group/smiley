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
    Map()
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