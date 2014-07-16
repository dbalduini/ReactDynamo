package io.react2.reactdynamo

import com.amazonaws.services.dynamodbv2.model._

class Table(
    val name: String,
    val hashPK: PK,
    val rangePK: Option[PK]=None,
    reads: Long = 10L,
    writes: Long = 5L) {
  
  val keySchema = new KeySchemaElement().withAttributeName(hashPK._1).withKeyType(KeyType.HASH)

  val provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(reads).withWriteCapacityUnits(writes)


}