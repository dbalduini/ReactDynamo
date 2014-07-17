package io.react2.reactdynamo

import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.Condition

class RichCondition(key: String) {

  def filter[T](comparison: ComparisonOperator, attr: AttrValue): Filter =
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