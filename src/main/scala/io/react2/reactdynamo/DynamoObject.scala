package io.react2.reactdynamo

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

