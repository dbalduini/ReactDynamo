package io.react2.reactdynamo.fix

import io.react2.reactdynamo._

trait ClientFix {
  this: UserFix =>

  case class Client(user: User, name: String, prefix: Int)

  object Client {

    private val fixtures: Map[String, Client] = Map(
      "sample" -> Client(User.gimme("sample"), "React2.io", 555))

    def gimme(alias: String): Client = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  /*
   * +----------------------------+
   * |userID  | client    |prefix |
   * |----------------------------|
   * | 1      | React2.io | 555   |
   * | 1      | XYZ.com   | 000   |
   * +----------------------------+
   */
  implicit object ClientDO extends DynamoObject[Client] with LazyLoading {
    import Implicits._

    val tableName = "Client"
    val hashPK = "userID" -> AttributeType.String
    val rangePK = Some("client" -> AttributeType.String)

    def toItem(t: Client): Item = obj(
      key("userID").value(t.user.name),
      key("client").value(t.name),
      key("prefix").value(t.prefix))

    lazy val user: String => User = (key: String) =>
      eager[User](Map("name" -> write(key)))

    def fromItem(item: Item): Client =
      Client(user(item("userID").read[String]),
        item("client").read[String],
        item.get("prefix").read[Int])

  }

}

