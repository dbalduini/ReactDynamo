package io.react2.reactdynamo

import akka.actor.{PoisonPill, ActorRef, ActorSystem}
import com.amazonaws.AmazonClientException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentials, BasicAWSCredentials}
import com.amazonaws.regions.{Regions, Region}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient

import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

class DynamoDriver(_system: Option[ActorSystem]) {

  def this() = this(Some(DynamoDriver.defaultSystem))

  lazy val system = _system.getOrElse(DynamoDriver.defaultSystem)

  /**
   * Connect to local a local DynamoDB.
   * @return a connected dynamo client
   */
  def local(): DynamoClient = {
    val basic = new BasicAWSCredentials("", "")
    val client = new AmazonDynamoDBClient(basic)
    client.setEndpoint("http://localhost:8000")
    new DynamoClient(client, actorFor(client))
  }

  /**
   *
   * Connect to an Aws DynamoDB.
   *
   * Default Region is US East (N. Virginia).
   *
   * @see [[com.amazonaws.regions.Regions Available Regions]]
   * @return a connected dynamo client
   */
  def connection(region: Regions = Regions.US_EAST_1): DynamoClient =
    DynamoDriver.credentials match {
      case Success(credentials) =>
        val client = new AmazonDynamoDBClient(credentials)
        client.setRegion(Region.getRegion(region))
        new DynamoClient(client, actorFor(client))
      case Failure(ex) => ex.printStackTrace(); throw ex;
    }

  /**
   * Shutdown the actor system.
   */
  def shutdown(): Unit = system.shutdown()

  /**
   * Shutdown the actor system and close resources.
   */
  def shutdownAll(client: DynamoClient): Unit = {
    shutdown()
    client.terminate()
  }

  private [reactdynamo] def actorFor(client: AmazonDynamoDBClient) =
    system.actorOf(DynamoConnection.props(client))

}

object DynamoDriver {

  private lazy val defaultSystem = {
    import com.typesafe.config.ConfigFactory
    val config = ConfigFactory.load()
    ActorSystem("reactdynamo")
  }

  private[reactdynamo] lazy val credentials: Try[AWSCredentials] =
    Try(new ProfileCredentialsProvider().getCredentials).recoverWith {
      case NonFatal(t) => throw new AmazonClientException(
        "Cannot load the credentials from the credential profiles file. " +
          "Please make sure that your credentials file is at the correct " +
          "location (~/.aws/credentials), and is in valid format.", t);
    }

}

class DynamoClient(val db: AmazonDynamoDBClient, val clientRef: ActorRef) extends api.DynamoOps {

  /**
   * Release any resources that might be held open.
   */
  def terminate(): Unit = {
    clientRef ! PoisonPill
    db.shutdown()
  }

}