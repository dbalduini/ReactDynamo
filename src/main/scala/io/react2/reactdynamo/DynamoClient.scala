package io.react2.reactdynamo

import akka.actor.ActorRef
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import akka.actor.ActorSystem

class DynamoDriver(_system: Option[ActorSystem]) {

  def this() = this(Some(DynamoDriver.defaultSystem))

  lazy val system = _system.getOrElse(DynamoDriver.defaultSystem)

  def local(): DynamoClient = {
    val basic = new BasicAWSCredentials("", "")
    val client = new AmazonDynamoDBClient(basic)
    client.setEndpoint("http://localhost:8000")
    new DynamoClient(client, system.actorOf(DynamoConnection.props(client)))
  }

  def connection(): DynamoClient = ???
  
}

object DynamoDriver {

  private lazy val defaultSystem = {
    import com.typesafe.config.ConfigFactory
    val config = ConfigFactory.load()
    ActorSystem("reactdynamo")
  }

}

class DynamoClient(val db: AmazonDynamoDBClient, val clientRef: ActorRef) extends api.DynamoOps