package com.boldradius.sdf.akka

import akka.actor.Props
import akka.testkit.TestProbe

class RequestSimulationExampleAppSpec  extends BaseAkkaSpec{

  "Creating RequestSimulationExampleApp" should {
    "result in creating a top-level actors named 'producerActor' and 'consumerActor' and 'statsActor' " in {
      val consumer = system.actorOf(Props(new RequestSimulationExampleApp), "sim")
      TestProbe().expectActor("/user/producerActor")
      TestProbe().expectActor("/user/consumerActor")
      TestProbe().expectActor("/user/statsActor")
    }


  }

}
