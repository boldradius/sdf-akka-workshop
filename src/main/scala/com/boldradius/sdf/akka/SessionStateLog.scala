package com.boldradius.sdf.akka

import akka.actor._
import com.boldradius.sdf.akka.SessionStateLog.{Tick, GetLog}
import scala.concurrent.duration._

object SessionStateLog {
  def props(sessionId: Long, inactiveTimeout:FiniteDuration):Props = Props(new SessionStateLog(sessionId,inactiveTimeout))

  case object GetLog

  case object Tick
}

class SessionStateLog(sessionId: Long, timeout:FiniteDuration) extends Actor with ActorLogging{
  override def receive: Receive = logRequests(List.empty,None)

  import context.dispatcher


  def logRequests(requestLog: List[Request], timer: Option[Cancellable]):Receive = {

    case r:Request =>
      // stop current timer if exists
      timer.fold({})(_.cancel())
      context.become(logRequests(r :: requestLog, Some(startTimer)))

    case GetLog => sender() ! requestLog


    case Tick =>
      context.parent ! Consumer.SessionInactive(sessionId, requestLog)

    case other => log.error(s"Unhandled msg: $other")

  }


  def startTimer = context.system.scheduler.scheduleOnce(timeout, self,Tick)


}
