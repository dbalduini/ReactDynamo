package io.react2.reactdynamo


import org.specs2.time.NoTimeConversions

import akka.util.Timeout

import scala.concurrent.duration._

import com.amazonaws.services.dynamodbv2.util.Tables
import com.amazonaws.services.dynamodbv2.model.GetItemRequest

import scala.collection.JavaConverters._
import org.specs2.mutable.Specification

import fix.UserAccountFix

import scala.concurrent.ExecutionContext.Implicits.global

class IntegrationSpec extends Specification with UserAccountFix with NoTimeConversions with AsyncMatchers
  with Format {

  import adapter.DurationBridge._

  implicit val timeout: Timeout = Timeout(2 seconds)
  val duration = scala.concurrent.duration.Duration("5 seconds")

  val driver = new DynamoDriver
  val client = driver.local()
  
  
  //TODO REFATORAR PARA LANCAR UMA VERSAO BACANA
  "IntegrationSpec" should {

    "Join all futures from a complete CRUD of an User" in {
      val account = UserAccount.gimme("sample1")

      val keyEntry = KeyEntry(UserAccountDO.hashPK._1)(_.withS(account.userId))

      def isNewTable = !Tables.doesTableExist(client.db, UserAccountDO.tableName)

      if (isNewTable){
        val f = await(client.createTable(Table.at[UserAccount]), duration)
        println(f)
      }

      await(client.putItem(account), duration)
      
      val hash = Map("UserId" -> attr(account.userId))
      val range = Map("uuid" -> attr(account.uuid))
      
      val keys = (hash ++ range)
      
//      val result = for {
//        put <- client.putItem(account)
//        get <- client.getItem[UserAccount](Map(
//            "UserId" -> value(account.userId),
//            "uuid"   -> value(account.uuid)
//            ))
//        del <- client.deleteItem(keyEntry)
//      } yield get
//
//      result.onComplete {
//        case _ => if (!isNewTable) client.deleteTable(UserAccountDO.tableName)
//      }
//
//      val dbUser = await(result, duration)
      
      //println(client.db.getItem(new GetItemRequest().withTableName(UserAccountDO.tableName).withKey(keys) ))
      
      println(await(client.getItem[UserAccount](keys), duration))
      
      val dbUser = Option(null)
      println("INTEGRATION USER " + dbUser)
      dbUser must not beNull
    }

  }

}