// Copyright 2015 Imprivata Inc.
package com.boldradius.sdf.akka

import scala.concurrent.duration._

import akka.actor.{Actor, ActorSystem}

trait ConfigHelper { this: Actor =>

  def finiteDuration(key: String): FiniteDuration =
    FiniteDuration(context.system.settings.config.getDuration(key, MILLISECONDS), MILLISECONDS)


}
