package io.react2.reactdynamo.protocol

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model._

object ItemCommands {

  case class PutItem(request: PutItemRequest) extends DynamoCommand[PutItemResult] {
    def execute(client: AmazonDynamoDBClient) = client.putItem(request)
  }

  case class GetItem(request: GetItemRequest) extends DynamoCommand[GetItemResult] {
    def execute(client: AmazonDynamoDBClient) = client.getItem(request)
  }
  case class DeleteItem(request: DeleteItemRequest) extends DynamoCommand[DeleteItemResult] {
    def execute(client: AmazonDynamoDBClient) = client.deleteItem(request)
  }

}