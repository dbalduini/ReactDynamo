package io.react2.reactdynamo

import scala.annotation.implicitNotFound

@implicitNotFound(
  "No DynamoDB serializer found for type ${A}. Try to implement an implicit Writes for this type.")
trait Writes[-A] {
  def writes(a: A): AttributeValue
}

trait DefaultWrites {

  implicit object StringWrites extends Writes[String] {
    def writes(s: String): AttributeValue = new AttributeValue().withS(s)
  }

  implicit object IntWrites extends Writes[Int] {
    def writes(i: Int): AttributeValue = new AttributeValue().withN(i.toString)
  }

  implicit object LongWrites extends Writes[Long] {
    def writes(i: Long): AttributeValue = new AttributeValue().withN(i.toString)
  }

  implicit object DoubleWrites extends Writes[Double] {
    def writes(i: Double): AttributeValue = new AttributeValue().withN(i.toString)
  }

  implicit object CharWrites extends Writes[Char] {
    def writes(c: Char): AttributeValue = new AttributeValue().withS(c.toString)
  }

}