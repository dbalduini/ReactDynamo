package io.react2.reactdynamo

trait DynamoFormat[T] {
  import types._
  
  def writes(t: T): DynamoObject
  def reads(o: DynamoObject): T
}


