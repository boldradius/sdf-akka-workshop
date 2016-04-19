// Copyright 2015 Imprivata Inc.
package com.boldradius.sdf.akka

import scala.concurrent.duration._
import scala.util.Random

trait ProbabilityUtils {

  protected def withProbability[A](probability: Double, positive: A, negative: A): A =
    if(Random.nextDouble() < probability){
      positive
    } else {
      negative
    }

  // For more interesting data, we insert some deviation in our numbers
  protected def deviate(duration: FiniteDuration):FiniteDuration = {
    val newDuration = duration * (1.5 - Random.nextDouble)
    FiniteDuration(newDuration.toMillis, MILLISECONDS)
  }

}
