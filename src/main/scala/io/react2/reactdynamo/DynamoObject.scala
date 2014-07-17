package io.react2.reactdynamo

trait DynamoObject[T] extends Format {

  val tableName: String
  val hashPK: PK
  val rangePK: Option[PK]
  
  def toItem(t: T): Item
  def fromItem(item: Item): T
}


private[reactdynamo] class RichAttrValue(attr: AttrValue) {
  def read[T: Reads]: T = implicitly[Reads[T]].reads(attr)
}