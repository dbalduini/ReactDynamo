package io.react2.reactdynamo

import org.specs2.mutable._
import org.specs2.specification._
import org.specs2.time.NoTimeConversions
import scala.concurrent.duration._
import akka.util.Timeout
import fix._
import com.amazonaws.services.dynamodbv2.util.Tables

trait DynamoSpec extends Specification with Blockable with NoTimeConversions
  with UserFix with UserAccountFix {

  import adapter.DurationBridge._

  lazy val dbSetup = databaseSetup
  implicit val timeout: Timeout = Timeout(2 seconds)
  val duration = 5 seconds
  val driver = new DynamoDriver
  val client = driver.local()

  override def map(fs: => Fragments) = Step(dbSetup.createDb) ^ fs ^ Step(dbSetup.cleanDb)

  object databaseSetup {

    lazy val allTables = List(
      Table[User],
      Table[UserAccount])

    def isNewTable(name: String) = !Tables.doesTableExist(client.db, name)

    lazy val createDb = {
      val tables = allTables.filter(t => isNewTable(t.name))
      println("creating the database: " + tables.mkString)
      tables foreach createTable
    }

    lazy val cleanDb = {
      allTables foreach { t => dropTable(t.name) }
      println("creating the database")
    }

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

  }

}
