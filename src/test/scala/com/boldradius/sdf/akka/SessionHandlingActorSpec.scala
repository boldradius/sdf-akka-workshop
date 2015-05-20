package com.boldradius.sdf.akka

import akka.actor.ActorDSL._
import akka.testkit.{TestProbe, EventFilter}

class SessionHandlingActorSpec extends BaseAkkaSpec {
  "Inactive session after 10 milliseconds" should {
    "should stop that SessionHandlingActor" in {
      val requestActor = actor("request-consumer")(new RequestConsumer)
      requestActor ! Request(0, 0, "url", "refer", "browser")

      val probe = TestProbe()
      val sessionHandler = probe.expectActor("/user/request-consumer/$a")
      probe.watch(sessionHandler)
      probe.expectTerminated(sessionHandler)

    }
  }
}
