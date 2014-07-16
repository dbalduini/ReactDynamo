package io.react2.reactdynamo.api

import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future

import com.amazonaws.services.dynamodbv2.model._
import io.react2.reactdynamo.protocol.ItemCommands._

trait ItemOps {
  this: DynamoOps =>

  def putItem(request: PutItemRequest)(implicit timeout: Timeout): Future[PutItemResult] =
    (clientRef ? PutItem(request)).mapTo[PutItemResult]

  def getItem(request: GetItemRequest)(implicit timeout: Timeout) =
    (clientRef ? GetItem(request)).mapTo[GetItemResult]

  def update = ???
  def delete = ???

}