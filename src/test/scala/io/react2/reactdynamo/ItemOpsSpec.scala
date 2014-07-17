package io.react2.reactdynamo

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap
import org.specs2.mutable._
import com.amazonaws.services.dynamodbv2.model._

import akka.util.Timeout
import fix.UserFix
import org.specs2.time.NoTimeConversions

class ItemOpsSpec extends DynamoSpec with UserFix with NoTimeConversions {

  val tableName = "Users"
  val userId = java.util.UUID.randomUUID.toString

  val user = User(userId, 1989)

  sequential

  "ItemOpsSpec" should {

    "Put an Item in DynamoDB" in {
      val f = client.putItem(user)
      val r = await(f, duration)
      println("Put Item: " + r)
      r must not beNull
    }

//    "Get an Item in DynamoDB" in {
//      val f = client.getItem[User](KeyEntry(UserDynamoObject.hashPK._1)(_.withS(userId)))
//      val r = await(f, duration)
//      println("Get Item: " + r)
//      r must_== user
//    }
//
//    "Delete an Item in DynamoDB" in {
//      val f = client.deleteItem(KeyEntry(UserDynamoObject.hashPK._1)(_.withS(userId)))
//      val r = await(f, duration)
//      println("Delete Item: " + r)
//      r must not beNull
//    }

  }

}