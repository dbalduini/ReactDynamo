package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification
import io.react2.reactdynamo._

import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future

trait UserFix {

  case class User(name: String, age: Int, genre: Option[Char])

  object User {

    private val fixtures: Map[String, User] = Map(
      "male" -> User("Sample User 1", 25, Some('M')),
      "sample" -> User("Sample User 2", 20, None),
      "female" -> User("Sample User 3", 17, Some('F')),
      "yong" -> User("Yong User", 5, None),
      "adult" -> User("Adult User", 30, Some('M')))

    def gimme(alias: String): User = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  implicit object UserDO extends DynamoObject[User] {
    import Implicits._

    val tableName = "User"
    val hashPK = ("name", AttributeType.String)
    val rangePK = None

    def toItem(t: User): Item = obj(
      key("name").value(t.name),
      key("age").value(t.age),
      key("genre").valueOpt(t.genre))

    def fromItem(item: Item): User = User(
      item("name").read[String],
      item("age").read[Int],
      item.get("genre").readOpt[Char])

  }
  
  object UserDAO extends Format {
    
    val timeout: Timeout = Timeout(2 seconds)
    val client = new DynamoDriver().local
    
    def findById(id: String): Future[Option[User]] = 
      client.getItem( Map("name" -> write(id)))(timeout, UserDO)
    
  }

}

