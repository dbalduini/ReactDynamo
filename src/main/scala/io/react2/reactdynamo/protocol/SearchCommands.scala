package io.react2.reactdynamo.protocol

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.amazonaws.services.dynamodbv2.model.ScanResult
import com.amazonaws.services.dynamodbv2.model.QueryRequest
import com.amazonaws.services.dynamodbv2.model.QueryResult

object SearchCommands {

  case class Scan(request: ScanRequest) extends DynamoCommand[ScanResult] {
    def execute(client: AmazonDynamoDBClient) = client.scan(request)
  }

  case class Query(request: QueryRequest) extends DynamoCommand[QueryResult] {
    def execute(client: AmazonDynamoDBClient) = client.query(request)
  }

}