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

trait EagerLoading {

  def duration: FiniteDuration

  //  @deprecated("not safe")
  //  def save[T](e: T)(r: T => KeyValue#Pair)(implicit dyo: DynamoObject[T]): KeyValue#Pair = {
  //    client.putItem(e)(timeout, dyo)
  //    r(e)
  //  }

  def eager[T](f: Future[Option[T]]): T = {
    val maybe: Option[T] = Await.result(f, duration).asInstanceOf[Option[T]]
    maybe.getOrElse(throw BrokenRelationshipException)
  }

}
