package io.react2.reactdynamo

import akka.util.Timeout
import scala.concurrent.{ Future, Await }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import io.react2.reactdynamo.exceptions.BrokenRelationshipException

trait DynamoObject[T] extends Format {
  val tableName: String
  val hashPK: PK
  val rangePK: Option[PK]

  def toItem(t: T): Item
  def fromItem(item: Item): T

  def obj(pairs: KeyValue#Pair*): Item = (for {
    pair <- pairs if pair.isDefined
  } yield pair.get).toMap

}

trait LazyLoading {

  def duration = 1 second

  implicit val timeout: Timeout = new Timeout(duration)

  val client = new DynamoDriver().local

  @deprecated("not safe")
  def save[T](e: T)(r: T => KeyValue#Pair)(implicit dyo: DynamoObject[T]): KeyValue#Pair = {
    client.putItem(e)(timeout, dyo)
    r(e)
  }

  def eager[T](key: Item)(implicit dyo: DynamoObject[T]): T = {
    val future = client.getItem(key)(timeout, dyo).map(_.getOrElse(throw BrokenRelationshipException))
    Await.result(future, timeout.duration).asInstanceOf[T]
  }

}
