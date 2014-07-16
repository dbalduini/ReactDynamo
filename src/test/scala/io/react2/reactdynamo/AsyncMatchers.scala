package io.react2.reactdynamo

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration

trait AsyncMatchers {

  def await[T](f: Future[T], duration: Duration): T = Await.result(f, duration)

}