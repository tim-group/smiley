package elasticsearch

import org.joda.time.LocalDate

object Happiness extends Enumeration {
    type Happiness = Value
    val Happy, Sad, Straight = Value
}
import Happiness._
  
class Repository {

  def getLastTwoWeeks() : Map[String, Map[LocalDate, Happiness]] = {
    Map()
  }
  
  def recordHappiness(user: String, date: LocalDate, level: Happiness) = {
  }
}