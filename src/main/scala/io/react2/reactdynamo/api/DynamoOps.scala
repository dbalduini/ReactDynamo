package io.react2.reactdynamo.api

import akka.actor.ActorRef

private[reactdynamo] trait DynamoOps
  extends TableOps
  with ItemOps
  with SearchOps
  with BatchOps {

  def clientRef: ActorRef

}