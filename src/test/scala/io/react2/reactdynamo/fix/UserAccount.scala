package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import io.react2.reactdynamo.Item

import io.react2.reactdynamo._

trait UserAccountFix {
  this: Specification =>

  case class UserAccount(userId: String, uuid: String, cash: Double)

  object UserAccount {

    private val fixtures = Map(
      "sample1" -> UserAccount("Sample User 1", "some random uuid", 2000.55))

    def gimme(alias: String): UserAccount = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  implicit object UserAccountDO extends DynamoObject[UserAccount] {
    val tableName = "UserAccount"
    val hashPK = ("UserId", AttributeType.String)
    val rangePK = Some(("uuid", AttributeType.String))

    def toItem(t: UserAccount): Item = Map(
      "UserId" -> value(t.userId),
      "uuid" -> value(t.uuid),
      "cash" -> value(t.cash))

    def fromItem(item: Item): UserAccount = UserAccount(
      item("UserId").read[String],
      item("uuid").read[String],
      item("cash").read[Double])

  }

}

