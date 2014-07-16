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

  def newItem(name: String, age: Int) = {
    val item = new HashMap[String, AttributeValue]();
    item.put("UserId", new AttributeValue(name));
    item.put("age", new AttributeValue().withN(Integer.toString(age)));
    item
  }

  sequential

  "ItemOpsSpec" should {

    "Put an Item in DynamoDB" in {
      val item = newItem(userId, 1989)
      println(item)
      val request = new PutItemRequest(tableName, item.asJava);
      val f = client.putItem(request)
      val r = await(f, duration)
      println("Result: " + r)
      r must not beNull
    }

    "Get an Item in DynamoDB" in {
      val request = new GetItemRequest()
        .withTableName(tableName)
        .addKeyEntry("UserId", new AttributeValue().withS(userId))
      val f = client.getItem(request)
      val r = await(f, duration)
      println("Result: " + r)
      r must not beNull
    }

    //
    //    "Delete an Item in DynamoDB" in {
    //    	
    //    }

  }

}