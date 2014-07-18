package io.react2.reactdynamo

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration

trait Blockable {

  def await[T](f: Future[T], duration: Duration): T = Await.result(f, duration)

}