package io.react2.reactdynamo
package api

import com.amazonaws.services.dynamodbv2.model.PutItemResult
import akka.util.Timeout
import scala.concurrent.Future
import scala.collection.JavaConverters._
import com.amazonaws.services.dynamodbv2.model.ScanResult
import akka.pattern.ask
import com.amazonaws.services.dynamodbv2.model.ScanRequest

import scala.concurrent.ExecutionContext.Implicits.global

trait SearchOps {
  this: DynamoOps =>

  import protocol.SearchCommands._
  import SearchOps._

  def scan[T](f: Filter)(implicit timeout: Timeout, obj: DynamoObject[T]): Future[List[T]] = {
    val request = scanRequest(obj.tableName, f)
    (clientRef ? Scan(request)).mapTo[ScanResult] map {
      result =>
        result.getItems.asScala.toList.map(item => obj.fromItem(item.asScala.toMap))
    }
  }

  def query = ???

}

private[this] object SearchOps {

  def scanRequest(tableName: String, f: Filter) =
    new ScanRequest().withTableName(tableName).withScanFilter(f.asJava)

}