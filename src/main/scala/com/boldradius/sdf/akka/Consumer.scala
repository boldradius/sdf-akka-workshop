package com.boldradius.sdf.akka

import akka.actor._
import com.boldradius.sdf.akka.Consumer.SessionInactive

import scala.concurrent.duration._


object Consumer {

  def props(inactiveTimeout:FiniteDuration):Props = Props(new Consumer(inactiveTimeout))

  case class SessionInactive(sessionId: Long, stats:List[Request])

}

class Consumer(inactiveTimeout:FiniteDuration) extends Actor with ActorLogging{
  override def receive: Receive = withSessionLog(Map.empty)


  def withSessionLog(sessionlogMap: Map[Long, ActorRef]): Receive = {

    case r:Request =>
      log.info("Received Request")
      sessionlogMap.get(r.sessionId).fold[Unit]({
        val newSessionLogActor = context.actorOf(SessionStateLog.props(r.sessionId, inactiveTimeout))
        newSessionLogActor ! r
        context.become(withSessionLog( sessionlogMap + (r.sessionId -> newSessionLogActor) ))
      }
      )( sessionLogActorRef => sessionLogActorRef ! r )

    case SessionInactive(sessionId, stats) =>

      println("SessionInactive sessionId = " + sessionId)

      sessionlogMap.get(sessionId).fold[Unit]({

        println("^^^^^^^^^^^^^^  no found sessionId")

      }){sessionLogActorRef =>

       println("***********  sending to stats")

       context.parent ! Stats.SessionStats(sessionId, stats)

       context.stop(sessionLogActorRef)
       context.become(withSessionLog(sessionlogMap - sessionId))

      }
  }

}
