package io.react2.reactdynamo
package api

import com.amazonaws.services.dynamodbv2.model._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future
import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._
import types._
import protocol.ItemCommands._

import scala.reflect.ClassTag

trait ItemOps {
  this: DynamoOps =>

  import ItemOps._

  def putItem[T](t: T)(implicit timeout: Timeout, format: DynamoFormat[T]): Future[PutItemResult] = {
    val request = putItemRequest(format.writes(t))
    (clientRef ? PutItem(request)).mapTo[PutItemResult]
  }

  def getItem[T](key: KeyEntry)(implicit timeout: Timeout, format: DynamoFormat[T], tag: ClassTag[T]): Future[GetItemResult] = {
    val tableName = tag.runtimeClass.getSimpleName
    val request = getItemRequest(tableName, key)
    (clientRef ? GetItem(request)).mapTo[GetItemResult]
  }

  def update = ???
  def delete = ???

}

private[this] object ItemOps {
  
  def putItemRequest(dynamoObject: DynamoObject): PutItemRequest = {
    val item = HashMap[String, AttributeValue]()
    dynamoObject._2.map {
      case (key, value) => item.put(key, value);
    }
    new PutItemRequest(dynamoObject._1, item.asJava)
  }

  def getItemRequest(tableName: String, key: KeyEntry): GetItemRequest = {
    val request = new GetItemRequest()
      .withTableName(tableName)
      .addKeyEntry(key._1, key._2)
    request
  }
  
}