package io.react2.reactdynamo
package api

import com.amazonaws.services.dynamodbv2.model._

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.JavaConverters._

trait ItemOps {
  this: DynamoOps =>

  import protocol.ItemCommands._
  import ItemOps._

  def putItem[T](t: T)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[PutItemResult] = {
    val request = putItemRequest(obj.tableName, obj.toItem(t))
    (clientRef ? PutItem(request)).mapTo[PutItemResult]
  }

  def getItem[T](key: Item)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[T] = {
    val request = getItemRequest(obj.tableName, key)
    (clientRef ? GetItem(request)).mapTo[GetItemResult].map {
      result =>
        obj.fromItem(result.getItem.asScala.toMap)
    }
  }

  def deleteItem[T](key: Item)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[DeleteItemResult] = {
    val request = deleteItemRequest(obj.tableName, key)
    (clientRef ? DeleteItem(request)).mapTo[DeleteItemResult]
  }

  def update = ???

}

private[this] object ItemOps {

  def putItemRequest(tableName: String, item: Item): PutItemRequest =
    new PutItemRequest(tableName, item.asJava)

  def getItemRequest(tableName: String, key: Item): GetItemRequest =
    new GetItemRequest().withTableName(tableName).withKey(key.asJava)

  def deleteItemRequest(tableName: String, key: Item): DeleteItemRequest =
    new DeleteItemRequest().withTableName(tableName).withKey(key.asJava)

}