package io.react2.reactdynamo

import akka.actor.ActorRef
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import akka.actor.ActorSystem

object DynamoClient {

  def local(): DynamoClient = {
    val basic = new BasicAWSCredentials("", "")
    val client = new AmazonDynamoDBClient(basic)
    client.setEndpoint("http://localhost:8000")
    new DynamoClient(defaultSystem.actorOf(DynamoConnection.props(client)))

  }

  def apply(): DynamoClient = ???

  private def defaultSystem = {
    import com.typesafe.config.ConfigFactory
    val config = ConfigFactory.load()
    ActorSystem("reactdynamo")
  }

}

class DynamoClient(val clientRef: ActorRef) extends api.DynamoOps