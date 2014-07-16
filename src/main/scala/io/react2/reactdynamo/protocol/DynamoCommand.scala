package io.react2.reactdynamo.protocol

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient

abstract class DynamoCommand[T] {

  def execute(client: AmazonDynamoDBClient): T
  
}