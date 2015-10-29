package com.boldradius.sdf.akka

import akka.util.Timeout
import com.boldradius.sdf.akka.Stats.{GetRequestsPerBrowser, SessionStats}
import org.scalatest.concurrent.ScalaFutures
import akka.pattern.ask
import scala.concurrent.duration._

class StatsSpec extends BaseAkkaSpec  with ScalaFutures {


  "Collecting stats per session" should{
    "expose a GetRequestsPerBrowser aggregate" in{

      val statsActor = system.actorOf(Stats.props,"stats-request-browser")
      statsActor ! SessionStats(1, List(
      Request(0,0,"url","ref","browser1"),
        Request(0,0,"url","ref","browser1"),
          Request(0,0,"url","ref","browser2")
      ))

      statsActor ! SessionStats(1, List(
        Request(1,0,"url","ref","browser3")
      ))


      implicit val timeout:Timeout = Timeout(2 seconds)

      whenReady((statsActor ? GetRequestsPerBrowser).mapTo[Map[String,Int]])( map =>

        assert(map == Map( "browser1" -> 2, "browser2" -> 1,"browser3" -> 1 ))

      )

    }
  }


}
