package io.react2.reactdynamo

import org.specs2.mutable._

class ItemOpsSpec extends DynamoSpec with Format {
  val tableName = "Users"
  val userId = java.util.UUID.randomUUID.toString

  val user = User(userId, 1989, Some('M'))
  val account = UserAccount.gimme("sample1")

  sequential

  "ItemOpsSpec" should {

    "Put an User in DynamoDB" in {
      val f = client.putItem(user)
      val r = await(f, duration)
      println("Put User: " + r)
      r must not beNull
    }

    "Put an UserAccount in DynamoDB" in {
      val f = client.putItem(account)
      val r = await(f, duration)
      println("Put UserAccount: " + r)
      r must not beNull
    }

    "Get an User in DynamoDB" in {
      val key = Map("name" -> write(userId))
      val f = client.getItem[User](key)
      val r = await(f, duration)
      println("Get User: " + r)
      r must_== Some(user)
    }

    "Get an UserAccount in DynamoDB" in {
      val hash = Map("UserId" -> write(account.userId))
      val range = Map("uuid" -> write(account.uuid))
      val keys = (hash ++ range)
      val f = client.getItem[UserAccount](keys)
      val r = await(f, duration)
      println("Get UserAccount: " + r)
      r must_== Some(account)
    }

    "Delete an Item in DynamoDB" in {
      val key = Map("name" -> write(userId))
      val f = client.deleteItem[User](key)
      val r = await(f, duration)
      println("Delete Item: " + r)
      r must not beNull
    }

    "Return None when the item is not found" in {
      val key = Map("name" -> write("dont exist"))
      val f = client.getItem[User](key)
      val r = await(f, duration)
      println("Get User: " + r)
      r must beNone
    }

  }

}