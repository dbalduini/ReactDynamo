package io.react2.reactdynamo

import org.specs2.mutable._
import io.react2.reactdynamo.exceptions.ReadsException
import fix.UserFix
import io.react2.reactdynamo.fix.ClientFix

class ConvertionsSpec extends Specification with Format with UserFix {

  val user = User.gimme("sample")
  val item = Map("name" -> write(user.name), "age" -> write(user.age))

  "ConvertionsSpec" should {

    "Write a String to an Attribute Value" in {
      write("dadwa") must_== new AttributeValue().withS("dadwa")
    }

    "Read an Attribute Value to a String" in {
      read[String](new AttributeValue().withS("dadwa")) must_== "dadwa"
    }

    "Write an Int to an Attribute Value" in {
      write(10) must_== new AttributeValue().withN("10")
    }

    "Read an Attribute Value to an Int" in {
      read[Int](new AttributeValue().withN("10")) must_== 10
    }

    "Write an User to an Dynamo Object" in {
      val item = UserDO.toItem(user)
      println(item)
      item must_== item
    }

    "Read a Dynamo Object to an User" in {
      val userDO = UserDO.fromItem(item)
      println(userDO)
      userDO must_== user
    }

    "Fail to read an S AttrValue as Int Value" in {
      read[Int](new AttributeValue().withS("10")) must throwA[ReadsException]
    }

  }

}