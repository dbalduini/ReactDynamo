package io.react2.reactdynamo

import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap
import org.specs2.mutable._
import com.amazonaws.services.dynamodbv2.model._
import akka.util.Timeout
import org.specs2.time.NoTimeConversions
import fix.UserFix
import org.specs2.specification.BeforeExample

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator

class ScanOpsSpec extends DynamoSpec with UserFix with NoTimeConversions with DefaultReads with DefaultWrites with BeforeExample {
 
  import Implicits._
  
  val (adults, yongs) = User.toList.partition(_.age > 21)

  def before {
    adults ++ yongs foreach (client.putItem(_))
  }

  "ScanOpsSpec" should {

    "Find two old Users" in {
      val query = "age" > 20
      val f = client.scan(query)
      val r = await(f, duration)
      println("ADULT USERS " + r)
      r must haveLength(2)
    }

    "Find no yong Users" in {
      val query = "age" < 20
      val f = client.scan(query)
      val r = await(f, duration)
      println("YONG USERS " + r)
      r must contain(yongs.head)
    }

    "Find users with age between 15 and 30" in {
      val query = ("age" > 20) ++ ("age" < 30)
      val f = client.scan(query)
      val r = await(f, duration)
      println("USERS 15 << X << 30 " + r)
      r must haveLength(3)
    }

    "Find one user where age == 5" in {
      val f = client.scan(("age" === 5))
      val r = await(f, duration)
      r.headOption must beSome(User.gimme("yong"))
    }

  }

}

