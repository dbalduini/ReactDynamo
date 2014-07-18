package io.react2.reactdynamo
package api

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.util.Tables

import akka.pattern.ask
import akka.util.Timeout

trait TableOps {
  this: DynamoOps =>

  import protocol.TableCommands
  import TableCommands._

  def createTable(table: Table)(implicit timeout: Timeout) =
    (clientRef ? CreateTable(table)).mapTo[CreateTableResult]

  def deleteTable(name: String)(implicit timeout: Timeout) =
    (clientRef ? DeleteTable(name)).mapTo[DeleteTableResult]

  def describeTable = ???

  def updateTable = ???

}