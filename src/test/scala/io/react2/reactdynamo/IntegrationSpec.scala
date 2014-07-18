package io.react2.reactdynamo

import org.specs2.mutable.Specification
import scala.concurrent.ExecutionContext.Implicits.global

import fix.UserFix

class IntegrationSpec extends DynamoSpec with Format {

  "IntegrationSpec" should {

    "Join Futures in a for comprehension is run sequentially" in {
      val male = User.gimme("male")
      val female = User.gimme("female")

      val key = Map("name" -> write(male.name))

      val result = for {
        put1 <- client.putItem(male)
        put2 <- client.putItem(female)
        get <- client.getItem[User](key)
        del <- client.deleteItem[User](key)
      } yield get

      result onComplete {
        case x => println("Future's Join terminate with: " + x)
      }

      val dbUser = await(result, duration)

      dbUser must_== male
    }

  }

}