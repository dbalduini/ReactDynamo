package io.react2.reactdynamo

import exceptions.ReadsException
import scala.annotation.implicitNotFound

@implicitNotFound(
  "No DynamoDB serializer found for type ${A}. Try to implement an implicit Reads for this type.")
trait Reads[A] {
  def reads(attv: AttributeValue): A
}

trait DefaultReads {

  implicit object StringReads extends Reads[String] {
    def reads(attv: AttributeValue): String = Option(attv.getS).getOrElse(throw ReadsException(attv.toString))
  }

  implicit object IntReads extends Reads[Int] {
    def reads(attv: AttributeValue): Int =
      Option(attv.getN).map(_.toInt).getOrElse(throw ReadsException(attv.toString))
  }

  implicit object LongReads extends Reads[Long] {
    def reads(attv: AttributeValue): Long =
      Option(attv.getN).map(_.toLong).getOrElse(throw ReadsException(attv.toString))
  }

  implicit object DoubleReads extends Reads[Double] {
    def reads(attv: AttributeValue): Double =
      Option(attv.getN).map(_.toDouble).getOrElse(throw ReadsException(attv.toString))
  }

  implicit object CharReads extends Reads[Char] {
    def reads(attv: AttributeValue): Char =
      NotEmptyString(attv.getS).map(_.head).getOrElse(throw ReadsException(attv.toString))
  }

}