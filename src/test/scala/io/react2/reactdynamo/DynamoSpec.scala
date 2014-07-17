package io.react2.reactdynamo

import scala.concurrent.duration._
import org.specs2.mutable._
import org.specs2.specification._
import org.specs2.time.NoTimeConversions
import akka.util.Timeout
import io.react2.reactdynamo.fix.UserFix

trait DynamoSpec extends Specification with AsyncMatchers with NoTimeConversions with UserFix {
  lazy val dbSetup = databaseSetup
  override def map(fs: => Fragments) = Step(dbSetup.createDb) ^ fs ^ Step(dbSetup.cleanDb)

  import adapter.DurationBridge._

  implicit val timeout: Timeout = Timeout(2 seconds)
  val duration = scala.concurrent.duration.Duration("5 seconds")
  
  val driver = new DynamoDriver
  val client = driver.local()

  object databaseSetup {

    lazy val allTables = List(Table.at[User])

    def createTable(table: Table) = {
      val r = client.createTable(table)
      println(await(r, duration).getTableDescription)
      println("[ItemOpsSpec] Creating table " + table.name)
    }

    def dropTable(name: String) {
      val r = client.deleteTable(name)
      println(await(r, duration).getTableDescription)
      println("[ItemOpsSpec] Deleting table " + name)
    }

    lazy val createDb = {
      allTables foreach createTable
      println("creating the database")

    }

    lazy val cleanDb = {
      allTables foreach { t => dropTable(t.name) }
      println("creating the database")

    }
  }

}
