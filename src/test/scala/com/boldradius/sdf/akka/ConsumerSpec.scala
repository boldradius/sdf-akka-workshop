package com.boldradius.sdf.akka

import akka.testkit.{EventFilter, TestProbe}
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

//  "Not Sending Request to SessionStateLog within inactivetimeout" should {
//
//    "result in StatsActor receiving stats for inactive session" in {
//      val consumer = system.actorOf(Consumer.props(2 seconds), "consumer-test-3")
//
//      val stats = system.actorOf(Stats.props, "consumer-test-stats")
//      val inactivetimeout = 1 seconds
//
//      val waitFor = 2 seconds
//      val sessionStateLog = system.actorOf(SessionStateLog.props(0, inactivetimeout), "sessionStateLog-timeout-stats")
//      sessionStateLog ! Request(0, 1, "url", "referrer", "browser")
//      consumer.within(waitFor) {
//        consumer.expectMsg(Stats.SessionStats(0, List( Request(0, 1, "url", "referrer", "browser"))))
//      }
//    }
//
//  }




}
