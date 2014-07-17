package io.react2.reactdynamo

import org.specs2.mutable._
import io.react2.reactdynamo.fix.UserFix
import io.react2.reactdynamo.exceptions.ReadsException

class ConvertionsSpec extends Specification with DefaultWrites with DefaultReads with UserFix {

  def read[T: Reads](a: AttrValue): T = implicitly[Reads[T]].reads(a)

  def write[T: Writes](t: T): AttrValue = implicitly[Writes[T]].writes(t)

  "ConvertionsSpec" should {

    "Write a String to an Attribute Value" in {
      write("dadwa") must_== new AttrValue().withS("dadwa")
    }

    "Read an Attribute Value to a String" in {
      read[String](new AttrValue().withS("dadwa")) must_== "dadwa"
    }

    "Write an Int to an Attribute Value" in {
      write(10) must_== new AttrValue().withN("10")
    }

    "Read an Attribute Value to an Int" in {
      read[Int](new AttrValue().withN("10")) must_== 10
    }

    "Write an User to an Dynamo Object" in {
      val user = User.gimme("sample1")
      val item = UserDynamoObject.toItem(user)
      println(item)
      item must_== Map("name" -> write(user.name), "age" -> write(user.age))
    }

    "Read a Dynamo Object to an User" in {
      val user = User.gimme("sample1")
      val item = Map("name" -> write(user.name), "age" -> write(user.age))
      val userDO = UserDynamoObject.fromItem(item)
      println(userDO)
      userDO must_== user
    }

    "Fail to read an S AttrValue as Int Value" in {
      read[Int](new AttrValue().withS("10")) must throwA[ReadsException]
    }

  }

}