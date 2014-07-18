package io.react2

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.model.{ AttributeValue => JAttributeValue }

package object reactdynamo {

  type AttributeValue = JAttributeValue
  type AttributeType = ScalarAttributeType
  
  type PK = (String, ScalarAttributeType)
  type Filter = Map[String, Condition]
  type Item = Map[String, AttributeValue]
  type JItem = java.util.Map[String, AttributeValue]
  
  object AttributeType {
    val String = ScalarAttributeType.S
    val Number = ScalarAttributeType.N
    val Binary = ScalarAttributeType.B
  }

  object Implicits {
    implicit def string2RichCondition(key: String) = new RichCondition(key)
    implicit def attrv2RichAttributeValue(attv: AttributeValue): RichAttributeValue = new RichAttributeValue(attv)
    implicit def attrv2RichAttributeValue(attv: Option[AttributeValue]): RichAttributeValue = new RichAttributeValue(attv)
  }

  object NotEmptyString {
    def apply(s: String): Option[String] = Option(s) match {
      case Some(s) if !s.isEmpty => Some(s)
      case _ => None
    }
  }

  object NotEmptyItem {
    def apply(m: JItem): Option[JItem] = Option(m) match {
      case Some(m) if !m.isEmpty => Some(m)
      case _ => None
    }
  }

}
