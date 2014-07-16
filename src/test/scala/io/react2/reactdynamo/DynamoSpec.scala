package io.react2.reactdynamo

import org.specs2.mutable._
import org.specs2.specification._
import akka.util.Timeout
import scala.concurrent.duration._
import org.specs2.time.NoTimeConversions

trait DynamoSpec extends Specification with AsyncMatchers with NoTimeConversions {
  lazy val dbSetup = databaseSetup
  override def map(fs: => Fragments) = Step(dbSetup.createDb) ^ fs ^ Step(dbSetup.cleanDb)

  import adapter.DurationBridge._
  
  implicit val timeout: Timeout = Timeout(2 seconds)
  val duration = scala.concurrent.duration.Duration("5 seconds")
  val client = DynamoClient.local()
  
  
  object databaseSetup {

    def createTable(name: String) = {
      val r = client.createTable(new Table(name, "UserId" -> AttributeType.String))
      println(await(r, duration).getTableDescription)
      println("[ItemOpsSpec] Creating table " + name)
    }

    def dropTable(name: String) {
      val r = client.deleteTable(name)
      println(await(r, duration).getTableDescription)
      println("[ItemOpsSpec] Deleting table " + name)
    }

    lazy val createDb = {
      createTable("Users")
      println("creating the database")

    }

    lazy val cleanDb = {
      dropTable("Users")
      println("creating the database")

    }
  }

}
