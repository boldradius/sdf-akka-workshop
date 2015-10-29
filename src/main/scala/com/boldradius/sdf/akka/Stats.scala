package com.boldradius.sdf.akka

import akka.actor.{ActorLogging, Props, Actor}
import com.boldradius.sdf.akka.Stats.SessionStats


object Stats{

  def props:Props = Props(new Stats)

  case class SessionStats(sessionId:Long, stats:List[Request])

}

class Stats extends Actor with ActorLogging{

  override def receive: Receive = withStats(Map.empty)

  def withStats(sessions:Map[Long, List[Request]]):Receive = {

    case SessionStats(sessionId, stats) =>
      println("__________________________  SessionStats ")
      context.become(withStats(sessions + (sessionId -> stats)))

    case other => log.info("Unhandled msg:" + other)


  }


}
