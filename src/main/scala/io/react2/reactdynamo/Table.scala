package io.react2.reactdynamo

import com.amazonaws.services.dynamodbv2.model._

case class Table(
  val name: String,
  val hashPK: PK,
  val rangePK: Option[PK] = None,
  reads: Long = 10L,
  writes: Long = 5L) {

  val keySchema: List[KeySchemaElement] = {
    val hashElement = new KeySchemaElement().withAttributeName(hashPK._1).withKeyType(KeyType.HASH) :: Nil
    val rangeElement: List[KeySchemaElement] = rangePK match {
      case Some(range) => new KeySchemaElement().withAttributeName(range._1).withKeyType(KeyType.RANGE) :: Nil
      case None => Nil
    }
    hashElement ++ rangeElement
  }

  val provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(reads).withWriteCapacityUnits(writes)

}

object Table {

  def at[T](implicit dynamoObj: DynamoObject[T]) = {
    println("****** " + dynamoObj.rangePK)
    new Table(
      name = dynamoObj.tableName,
      hashPK = dynamoObj.hashPK,
      rangePK = dynamoObj.rangePK)
  }

}