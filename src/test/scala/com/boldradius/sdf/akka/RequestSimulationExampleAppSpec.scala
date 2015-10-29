package com.boldradius.sdf.akka

import akka.testkit.TestProbe

/**
 * Created by dave on 2015-10-28.
 */
class RequestSimulationExampleAppSpec  extends BaseAkkaSpec{

  "Creating RequestSimulationExampleApp" should {
    "result in creating a top-level actors named 'producerActor' and 'consumerActor' " in {
      new RequestSimulationExampleApp(system)
      TestProbe().expectActor("/user/producerActor")
      TestProbe().expectActor("/user/consumerActor")
    }


  }

}
