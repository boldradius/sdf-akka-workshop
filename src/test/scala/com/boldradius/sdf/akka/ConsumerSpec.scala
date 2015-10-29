package com.boldradius.sdf.akka

import akka.testkit.{EventFilter, TestProbe}


class ConsumerSpec extends BaseAkkaSpec{


  "Sending message to Consumer" should {
    "result in logging " in {
      EventFilter.info(pattern = ".*Received Request.*", occurrences = 1) intercept {
        val consumer = system.actorOf(Consumer.props, "consumerActor")
        consumer ! Request(0, System.currentTimeMillis(), "url", "referrer", "browser")
      }
    }
    "result in creation of SessionStateLog " in {
      val consumer = system.actorOf(Consumer.props, "consumer-test-2")
      consumer ! Request(1, System.currentTimeMillis(), "url", "referrer", "browser")
      TestProbe().expectActor("/user/consumer-test-2/$*")

    }
  }

}
