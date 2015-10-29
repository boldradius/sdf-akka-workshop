package com.boldradius.sdf.akka

import akka.actor.{ActorLogging, Props, Actor}
import com.boldradius.sdf.akka.Stats.{StatsAggregate, GetRequestsPerBrowser, SessionStats}


object Stats{

  def props:Props = Props(new Stats)

  case class SessionStats(sessionId:Long, stats:List[Request])


  case object GetRequestsPerBrowser


  case class StatsAggregate(requestsPerBrowser: Map[String, Int])


  def update(stats:StatsAggregate, requests: List[Request]):StatsAggregate =
  requests.foldLeft(stats)((acc,request) =>
    acc.copy(requestsPerBrowser =
      acc.requestsPerBrowser + (request.browser -> (acc.requestsPerBrowser.getOrElse(request.browser, 0) + 1))))



}

class Stats extends Actor with ActorLogging{

  override def receive: Receive = withStats(StatsAggregate(Map.empty))

  def withStats(stats:StatsAggregate):Receive = {

    case SessionStats(sessionId, requests) =>
      context.become(withStats(Stats.update(stats,requests)))


    case GetRequestsPerBrowser => sender() ! stats.requestsPerBrowser


    case other => log.info("Unhandled msg:" + other)


  }




}
