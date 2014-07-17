package io.react2.reactdynamo
package api

import com.amazonaws.services.dynamodbv2.model._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future
import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

trait ItemOps {
  this: DynamoOps =>

  import ItemOps._
  import protocol.ItemCommands._

  def putItem[T](t: T)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[PutItemResult] = {
    val request = putItemRequest(obj.tableName, obj.toItem(t))
    (clientRef ? PutItem(request)).mapTo[PutItemResult]
  }

  def getItem[T](key: Item)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[T] = {
    println("GET ITEM DEBUG")
    val tableName = obj.tableName
    val request = getItemRequest(tableName, key)
    println(request)
    val f = (clientRef ? GetItem(request)).mapTo[GetItemResult].map {
      result =>
        println(result)
        obj.fromItem(result.getItem.asScala.toMap)
    }
    f.onComplete {
      case t => println("GET ITEM  " + t)
    }
    f
  }

  def deleteItem[T](key: KeyEntry)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[DeleteItemResult] = {
    val tableName = obj.tableName
    val request = deleteItemRequest(tableName, key)
    (clientRef ? DeleteItem(request)).mapTo[DeleteItemResult]
  }

  def update = ???

}

private[this] object ItemOps {

  def putItemRequest(tableName: String, item: Item): PutItemRequest =
    new PutItemRequest(tableName, item.asJava)

  def getItemRequest(tableName: String, item: Item): GetItemRequest = {
	  val j = item.asJava
      val g = new GetItemRequest().withTableName(tableName).withKey(j)
      println("GET ITEM REQUEST  " + g)
      g
  }

  def deleteItemRequest(tableName: String, key: KeyEntry): DeleteItemRequest = key match {
    case (k, v) => new DeleteItemRequest().withTableName(tableName).addKeyEntry(key._1, key._2)
  }

}