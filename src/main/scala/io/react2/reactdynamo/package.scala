package io.react2

import com.amazonaws.services.dynamodbv2.model._

package object reactdynamo {

  type PK = (String, ScalarAttributeType)

  object AttributeType {
    val String = ScalarAttributeType.S
    val Number = ScalarAttributeType.N
    val Binary = ScalarAttributeType.B
  }

}