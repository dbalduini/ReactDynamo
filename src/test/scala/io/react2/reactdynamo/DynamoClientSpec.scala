package io.react2.reactdynamo

import akka.util.Timeout
import org.specs2.mutable._

import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class DynamoClientSpec extends Specification with Blockable {

  case class Test(name: String, num: Int)

  object Test {
    val table = new Table("Test", "UserId" -> AttributeType.String)
  }

  implicit val timeout = Timeout(FiniteDuration(5, "seconds"))

  val duration = scala.concurrent.duration.Duration("5 seconds")

  val driver = new DynamoDriver
  val client = driver.local()

  sequential

  "DynamoClientSpec" should {

    "Connect to a remote DynamoDB" in {
      Try(driver.connection().listTables(1)).isSuccess must beTrue
    }

    "Create Test Table" in {
      val f = client.createTable(Test.table)
      val desc = await(f, duration).getTableDescription
      println(desc)
      desc must not beNull
    }

    "Destroy Test Table" in {
      val f = client.deleteTable("Test")
      val desc = await(f, duration).getTableDescription
      println(desc)
      desc must not beNull
    }

  }

}