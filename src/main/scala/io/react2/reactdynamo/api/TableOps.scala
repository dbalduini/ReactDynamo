package io.react2.reactdynamo.api

import io.react2.reactdynamo.Table
import io.react2.reactdynamo.DynamoObject
import io.react2.reactdynamo.protocol.TableCommands
import akka.pattern.ask
import akka.util.Timeout
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.util.Tables

trait TableOps {
	this: DynamoOps =>
  
	import TableCommands._
	  
	def createTable(table: Table)(implicit timeout: Timeout) = 
	  (clientRef ? CreateTable(table)).mapTo[CreateTableResult]
	
	def deleteTable(name: String)(implicit timeout: Timeout) = 
	  (clientRef ? DeleteTable(name)).mapTo[DeleteTableResult]
	
	def describeTable = ???
	
	def updateTable = ???

	  
}