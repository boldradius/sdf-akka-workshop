package com.boldradius.sdf.akka

import akka.testkit.{TestActorRef, EventFilter, TestProbe}
import com.boldradius.sdf.akka.Consumer.SessionInactive
import scala.concurrent.duration._

class ConsumerSpec extends BaseAkkaSpec{


  "Sending message to Consumer" should {
    "result in logging " in {
      EventFilter.info(pattern = ".*Received Request.*", occurrences = 1) intercept {
        val consumer = system.actorOf(Consumer.props(2 seconds), "consumerActor")
        consumer ! Request(0, System.currentTimeMillis(), "url", "referrer", "browser")
      }
    }
    "result in creation of SessionStateLog " in {
      val consumer = system.actorOf(Consumer.props(2 seconds), "consumer-test-2")
      consumer ! Request(1, System.currentTimeMillis(), "url", "referrer", "browser")
      TestProbe().expectActor("/user/consumer-test-2/$*")

    }
  }

  "Receiving a SessionInactive msg" should {
    "result in sending a SessionStats msg to parent" in {
      val inactivetimeout = 1 seconds
      val waitFor = 2 seconds

      val parent = TestProbe()
      val underTest = TestActorRef[Consumer]( Consumer.props(inactivetimeout), parent.ref, "child-consumer")

      underTest ! Request(1, 1, "url", "referrer", "browser")
//      underTest ! SessionInactive(0,List( Request(0, 1, "url", "referrer", "browser") ))
      parent.within(waitFor) {
        parent.expectMsg(Stats.SessionStats(1, List( Request(1, 1, "url", "referrer", "browser"))))
      }
    }


  }




}
