package io.react2.reactdynamo

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import akka.actor._
import scala.concurrent.duration._
import protocol.DynamoCommand
import scala.util.control.NonFatal

object DynamoConnection {
  def props(client: AmazonDynamoDBClient) = Props(classOf[DynamoConnection], client)
}

private[reactdynamo] class DynamoConnection(client: AmazonDynamoDBClient) extends Actor with ActorLogging {
  import akka.actor.SupervisorStrategy._

  case class DynamoRequest(ref: ActorRef, cmd: DynamoCommand[_])

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case NonFatal(e) => Stop
      case _: Exception => Escalate
    }

  def receive = {
    case cmd: DynamoCommand[_] => worker ! DynamoRequest(sender, cmd)
    case m @ _ => log.info("Message not catch: " + m)
  }

  def worker = context.actorOf(Props(new Actor {
    def receive = {
      case DynamoRequest(lastSender, cmd) =>
        lastSender ! cmd.execute(client)
        self ! PoisonPill
      case m => log.info("Message not catch: " + m)
    }
  }))

}