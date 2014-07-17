package io.react2

import com.amazonaws.services.dynamodbv2.model._

package object reactdynamo {

  type PK = (String, ScalarAttributeType)
  type KeyEntry = (String, AttributeValue)

  type AttrValue = AttributeValue
  type Item = Map[String, AttrValue]

  type Filter = Map[String, Condition]

  def KeyEntry(key: String)(f: AttributeValue => AttributeValue): KeyEntry = {
    val value = new AttributeValue()
    (key, f(value))
  }

  object AttributeType {
    val String = ScalarAttributeType.S
    val Number = ScalarAttributeType.N
    val Binary = ScalarAttributeType.B
  }

  object Implicits {
    implicit def fromStringToCondition(key: String) = new RichCondition(key)
  }

}

