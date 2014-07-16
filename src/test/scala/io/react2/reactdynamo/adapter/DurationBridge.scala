package io.react2.reactdynamo.adapter

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.{Duration => ScalaDuration}

import org.specs2.time.{Duration => Specs2Duration}
import org.specs2.time.TimeConversions._

import java.util.concurrent.TimeUnit

/**
 * Created by dtbalduini on 06/02/14.
 */
object DurationBridge {

  implicit def fromDuration2FiniteDuration(d: Specs2Duration): FiniteDuration = new FiniteDuration(d.inMilliseconds, TimeUnit.MILLISECONDS)

  implicit def fromFiniteDuration2Duration(fd: FiniteDuration): Specs2Duration = fd.toMillis millis

  implicit def fromDuration2ScalaDuration(d: Specs2Duration): ScalaDuration = ScalaDuration.create(d.inMilliseconds, TimeUnit.MILLISECONDS)

  implicit def fromScalaDuration2Duration(sd: ScalaDuration): Specs2Duration = sd.toMillis millis

}
