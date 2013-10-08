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
  
  def recordHappiness(user: String, date: String, level: String) = {
    val newId = UUID.randomUUID()
    val data = Json.obj(
      "user" -> user,
      "date" -> date,
      "level" -> level
    )
    
    val config = Play.application().configuration()
    val username = config.getString("es.username")
    val password = config.getString("es.password")
    val baseUrl = config.getString("es.baseUrl")
    
    val response = WS.url(baseUrl + newId).withAuth(username, password, Realm.AuthScheme.BASIC).put(data)
    Await.result(response, Duration(10, scala.concurrent.duration.SECONDS))
  }
}