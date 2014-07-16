package io.react2.reactdynamo.fix

import org.specs2.mutable.Specification
import io.react2.reactdynamo.DynamoFormat
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import io.react2.reactdynamo.types._

trait UserFix {
  this: Specification =>

  case class User(name: String, age: Int)

  object User {

    val hashPK = "UserId"

    private val fixtures = Map(
      "sample1" -> User("Diego", 25))

    def gimme(alias: String): User = fixtures(alias)

    def toList = fixtures.valuesIterator.toList

  }

  
  //TODO PENSAR COMO VO COLOCAR O NOME DA TABELA JUNTO COM O DYNAMO OBJECT
  implicit val UserMapper = new DynamoFormat[User] {

    def writes(u: User): DynamoObject = (tableName, Map(
      "name" -> new AttributeValue().withS(u.name),
      "age" -> new AttributeValue().withN(u.age.toString)))

    def reads(o: DynamoObject): User = User(
      o._2("name").getS(),
      o._2("age").getN().toInt)

  }

}

