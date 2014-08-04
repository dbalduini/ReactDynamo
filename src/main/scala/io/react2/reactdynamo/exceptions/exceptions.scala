package io.react2.reactdynamo.exceptions

abstract class ReactDynamoException(msg: String) extends Exception(msg)

case class ReadsException(msg: String) extends ReactDynamoException(msg)

case object BrokenRelationshipException extends ReactDynamoException("Impossible to associate objects.")