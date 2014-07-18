package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification
import io.react2.reactdynamo._

trait UserAccountFix {

  case class UserAccount(userId: String, uuid: String, cash: Double)

  object UserAccount {

    private val fixtures = Map(
      "sample1" -> UserAccount("Sample User 1", "some random uuid", 2000.55))

    def gimme(alias: String): UserAccount = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  implicit object UserAccountDO extends DynamoObject[UserAccount] {
    import Implicits._
    
    val tableName = "UserAccount"
    val hashPK = ("UserId", AttributeType.String)
    val rangePK = Some(("uuid", AttributeType.String))
    
    def toItem(t: UserAccount): Item = Map(
      "UserId" -> write(t.userId),
      "uuid" -> write(t.uuid),
      "cash" -> write(t.cash))

    def fromItem(item: Item): UserAccount = UserAccount(
      item("UserId").read[String],
      item("uuid").read[String],
      item("cash").read[Double])

  }

}

