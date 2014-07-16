package io.react2

import com.amazonaws.services.dynamodbv2.model._

package object reactdynamo {

  type PK = (String, ScalarAttributeType)

  type KeyEntry = (String, AttributeValue)

  def KeyEntry(key: String)(f: AttributeValue => AttributeValue): KeyEntry = {
    val value = new AttributeValue()
    (key, f(value))
  }

  object AttributeType {
    val String = ScalarAttributeType.S
    val Number = ScalarAttributeType.N
    val Binary = ScalarAttributeType.B
  }

  object types {
    type DynamoObject = (String, Map[String, AttributeValue])
  }

}