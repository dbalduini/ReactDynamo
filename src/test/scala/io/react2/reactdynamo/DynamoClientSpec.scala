package io.react2.reactdynamo

import org.specs2.mutable._
import com.amazonaws.services.dynamodbv2.util.Tables
import org.specs2.matcher.ResultMatchers
import org.specs2.matcher.Matcher
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.{ Duration => SDuration }
import com.amazonaws.services.dynamodbv2.model.TableDescription
import com.amazonaws.services.dynamodbv2.model.PutItemResult
import akka.util.Timeout
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration

class DynamoClientSpec extends Specification with Blockable {

  case class Test(name: String, num: Int)

  object Test {
    val table = new Table("Test", "UserId" -> AttributeType.String)
  }

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(FiniteDuration(5, "seconds"))

  val duration = scala.concurrent.duration.Duration("5 seconds")

  val driver = new DynamoDriver
  val client = driver.local()

  sequential

  "DynamoClientSpec" should {

    "Create Test Table" in {
      val f = client.createTable(Test.table)
      val desc = await(f, duration).getTableDescription()
      println(desc)
      desc must not beNull
    }

    "Destroy Test Table" in {
      val f = client.deleteTable("Test")
      val desc = await(f, duration).getTableDescription()
      println(desc)
      desc must not beNull
    }

  }

}