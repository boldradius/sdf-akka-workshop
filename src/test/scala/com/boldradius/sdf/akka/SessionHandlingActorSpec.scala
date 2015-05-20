package com.boldradius.sdf.akka

import akka.actor.ActorDSL._
import akka.testkit.{TestProbe, EventFilter}

class SessionHandlingActorSpec extends BaseAkkaSpec {
  "Inactive session after 20 seconds" should {
    "should stop that SessionHandlingActor" in {
      val requestActor = actor("request-consumer")(new RequestConsumer)
      requestActor ! Request(0, 0, "url", "refer", "browser")

      TestProbe().expectActor("/user/request-consumer/$a")
    }
  }
}
