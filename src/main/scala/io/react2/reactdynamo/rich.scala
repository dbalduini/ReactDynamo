package io.react2.reactdynamo

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.Condition
import io.react2.reactdynamo.exceptions.ReadsException

class RichCondition(key: String) {

  def filter[T](comparison: ComparisonOperator, attr: AttributeValue): Filter =
    Map(key -> new Condition()
      .withComparisonOperator(comparison.toString)
      .withAttributeValueList(attr))

  def >[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.GT, w.writes(t))
  def <[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.LT, w.writes(t))
  def ===[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.EQ, w.writes(t))
  def >=[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.GE, w.writes(t))
  def <=[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.LE, w.writes(t))
  def !=[T](t: T)(implicit w: Writes[T]): Filter = filter(ComparisonOperator.NE, w.writes(t))

}

private[reactdynamo] class RichAttributeValue(attr: Option[AttributeValue]) {

  def this(attrv: AttributeValue) = this(Some(attrv))

  def read[T: Reads]: T = implicitly[Reads[T]].reads(attr.get) //TODO AJUSTAR ESSE GET

  def readOpt[T: Reads]: Option[T] = {
    try {
      attr.map((implicitly[Reads[T]].reads(_)))
    } catch {
      case ReadsException(msg) => None
    }
  }
}