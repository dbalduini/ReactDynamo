package io.react2.reactdynamo

import io.react2.reactdynamo.fix.ClientFix

class RelationshipSpec extends DynamoSpec with Format with ClientFix {

  val sample = Client.gimme("sample")

  sequential

  "RelationshipSpec" should {

    "Save the User dependency" in {
      val f = client.putItem(sample.user)
      val r = await(f, duration)
      r must not beNull
    }

    "Save the Client" in {
      val f = client.putItem(sample)
      val r = await(f, duration)
      r must not beNull
    }

    "Get the Client" in {
      val key = Map(
        "userID" -> write(sample.user.name),
        "client" -> write(sample.name))
      val f = client.getItem[Client](key)
      val r = await(f, duration)
      println(r)
      r.map(_.user) must beSome(sample.user)
    }

  }

}