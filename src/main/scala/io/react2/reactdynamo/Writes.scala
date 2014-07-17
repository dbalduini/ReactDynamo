package io.react2.reactdynamo

import scala.annotation.implicitNotFound

@implicitNotFound(
  "No DynamoDB serializer found for type ${A}. Try to implement an implicit Writes for this type.")
trait Writes[-A] {
  def writes(a: A): AttrValue
}

trait DefaultWrites {

  implicit object StringWrites extends Writes[String] {
    def writes(s: String): AttrValue = new AttrValue().withS(s)
  }

  implicit object IntWrites extends Writes[Int] {
    def writes(i: Int): AttrValue = new AttrValue().withN(i.toString)
  }

  implicit object DoubleWrites extends Writes[Double] {
    def writes(i: Double): AttrValue = new AttrValue().withN(i.toString)
  }

}