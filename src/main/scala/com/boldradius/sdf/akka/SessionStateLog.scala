package com.boldradius.sdf.akka

import akka.actor.{ActorLogging, Props, Actor}
import com.boldradius.sdf.akka.SessionStateLog.GetLog


object SessionStateLog {
  def props:Props = Props(new SessionStateLog)

  case object GetLog
}

class SessionStateLog extends Actor with ActorLogging{
  override def receive: Receive = logRequests(List.empty)

  def logRequests(requestLog: List[Request]):Receive = {

    case r:Request =>
      context.become(logRequests(r :: requestLog))

    case GetLog => sender() ! requestLog


    case other => log.error(s"Unhandled msg: $other")

  }



}
