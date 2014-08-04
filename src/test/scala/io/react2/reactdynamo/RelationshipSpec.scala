package io.react2.reactdynamo

import io.react2.reactdynamo.fix.ClientFix
import io.react2.reactdynamo.exceptions.BrokenRelationshipException
import scala.concurrent.ExecutionContext.Implicits.global

class RelationshipSpec extends DynamoSpec with Format with ClientFix {

  val sample = Client.gimme("sample")
  val broken = Client.gimme("broken")

  sequential

  "RelationshipSpec" should {

    "Save the User dependency" in {
      val f = client.putItem(sample.user)
      val r = await(f, duration)
      r must not beNull
    }

    "Save the Client" in {
      val f = for {
        s <- client.putItem(sample)
        o <- client.putItem(broken)
      } yield s
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

    "Fail to get the Client when relation is broken" in {
      val key = Map(
        "userID" -> write(broken.user.name),
        "client" -> write(broken.name))
      val f = client.getItem[Client](key)
      await(f, duration) must throwA(BrokenRelationshipException)
    }

  }

}