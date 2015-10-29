package com.boldradius.sdf.akka

import akka.testkit.{TestProbe, EventFilter}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import akka.pattern.ask
import scala.concurrent.duration._

class SessionStateLogSpec extends BaseAkkaSpec  with ScalaFutures {


  "Sending Request to SessionStateLog" should {
    "result in list incrementing" in {
        val sessionStateLog = system.actorOf(SessionStateLog.props, "sessionStateLog")
        sessionStateLog ! Request(0, 1, "url", "referrer", "browser")
        implicit val timeout:Timeout = Timeout(3 seconds)
        whenReady(  (sessionStateLog ? SessionStateLog.GetLog).mapTo[List[Request]] ){
          list => assert( list.contains(Request(0, 1, "url", "referrer", "browser")) )
        }
    }
  }



}
