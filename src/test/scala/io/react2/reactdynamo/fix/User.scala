package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import io.react2.reactdynamo.Item

import io.react2.reactdynamo._

trait UserFix {
  this: Specification =>

  case class User(name: String, age: Int)

  object User {

    private val fixtures = Map(
      "sample1" -> User("Sample User 1", 25),
      "sample2" -> User("Sample User 2", 20),
      "sample2" -> User("Sample User 3", 17),
      "yong" -> User("Yong User", 5),
      "adult" -> User("Adult User", 30))

    def gimme(alias: String): User = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  implicit object UserDynamoObject extends DynamoObject[User] {
    val tableName = "User"
    val hashPK = ("name", AttributeType.String)
    val rangePK = None

    def toItem(t: User): Item = Map(
      "name" -> value(t.name),
      "age" -> value(t.age))

    def fromItem(item: Item): User = User(
      item("name").read[String], item("age").read[Int])

  }

}

