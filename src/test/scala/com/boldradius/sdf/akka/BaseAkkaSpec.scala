/**
 * Copyright © 2014, 2015 Typesafe, Inc. All rights reserved. [http://www.typesafe.com]
 */

package com.boldradius.sdf.akka

import akka.actor.{ActorIdentity, ActorRef, ActorSystem, Identify}
import akka.testkit.{EventFilter, TestEvent, TestProbe}
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

abstract class BaseAkkaSpec extends BaseSpec with BeforeAndAfterAll {

  implicit class TestProbeOps(probe: TestProbe) {

    def expectActor(path: String, max: FiniteDuration = probe.remainingOrDefault): ActorRef = {
      probe.within(max) {
        var actor = null: ActorRef
        probe.awaitAssert {
          (probe.system actorSelection path).tell(Identify(path), probe.ref)
          probe.expectMsgPF(100 milliseconds) {
            case ActorIdentity(`path`, Some(ref)) => actor = ref
          }
        }
        actor
      }
    }
  }

  implicit val system = ActorSystem()
  system.eventStream.publish(TestEvent.Mute(EventFilter.debug()))
  system.eventStream.publish(TestEvent.Mute(EventFilter.info()))
  system.eventStream.publish(TestEvent.Mute(EventFilter.warning()))
  system.eventStream.publish(TestEvent.Mute(EventFilter.error()))

  override protected def afterAll(): Unit = {
    system.terminate()
    Await.result(system.whenTerminated,Duration.Inf)
  }
}
