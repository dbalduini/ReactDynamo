package io.react2.reactdynamo.protocol

import io.react2.reactdynamo._
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model._

object TableCommands {

  case class CreateTable(table: Table) extends DynamoCommand[CreateTableResult] {

    def execute(client: AmazonDynamoDBClient): CreateTableResult = client.createTable(createTableRequest)

    def defineAttribute(pk: PK) = new AttributeDefinition().withAttributeName(pk._1).withAttributeType(pk._2)

    lazy val createTableRequest = {
      val ctr = new CreateTableRequest().withTableName(table.name)
        .withKeySchema(table.keySchema: _*)
        .withAttributeDefinitions(defineAttribute(table.hashPK))
        .withProvisionedThroughput(table.provisionedThroughput)
      table.rangePK.map(range => ctr.withAttributeDefinitions(defineAttribute(range)))
      ctr
    }

  }

  case class DeleteTable(name: String) extends DynamoCommand[DeleteTableResult] {
    def execute(client: AmazonDynamoDBClient): DeleteTableResult = client.deleteTable(name)
  }

  case class ListTables(limit: Int) extends DynamoCommand[ListTablesResult] {
    override def execute(client: AmazonDynamoDBClient): ListTablesResult = client.listTables(limit)
  }

}