package io.react2.reactdynamo

import org.specs2.mutable._
import org.specs2.specification.BeforeExample

class ScanOpsSpec extends DynamoSpec with DefaultReads with DefaultWrites with BeforeExample {

  import Implicits._

  val (adults, yongs) = User.toList.partition(_.age > 21)

  def before {
    val all = adults ++ yongs
    println(all)
    all foreach (client.putItem(_))
  }

  "ScanOpsSpec" should {

    "Find two old Users" in {
      val query = "age" > 20
      val f = client.scan[User](query)
      val r = await(f, duration)
      println("ADULT USERS " + r)
      r must haveLength(2)
    }

    "Find no yong Users" in {
      val query = "age" < 20
      val f = client.scan[User](query)
      val r = await(f, duration)
      println("YONG USERS " + r)
      r must contain(yongs.head)
    }

    "Find adult male adult users" in {
      val query = (("age" > 18) ++ ("genre" === 'M'))
      val f = client.scan[User](query)
      val r = await(f, duration)
      r must haveLength(2)
    }

    "Find one user where age == 5" in {
      val f = client.scan[User](("age" === 5))
      val r = await(f, duration)
      r.headOption must beSome(User.gimme("yong"))
    }

  }

}

