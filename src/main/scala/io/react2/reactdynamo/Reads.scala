package io.react2.reactdynamo

import exceptions.ReadsException
import scala.annotation.implicitNotFound

@implicitNotFound(
  "No DynamoDB serializer found for type ${A}. Try to implement an implicit Reads for this type.")
trait Reads[A] {
  def reads(attr: AttrValue): A
}

trait DefaultReads {

  implicit object StringReads extends Reads[String] {
    def reads(attr: AttrValue): String = Option(attr.getS).getOrElse(throw ReadsException(attr.toString))
  }

  implicit object IntReads extends Reads[Int] {
    def reads(attr: AttrValue): Int =
      Option(attr.getN).map(_.toInt).getOrElse(throw ReadsException(attr.toString))
  }

  implicit object DoubleReads extends Reads[Double] {
    def reads(attr: AttrValue): Double =
      Option(attr.getN).map(_.toDouble).getOrElse(throw ReadsException(attr.toString))
  }

}