package com.boldradius.sdf.akka

import akka.actor.{ActorRef, Props, Actor, ActorLogging}


object Consumer {

  def props:Props = Props(new Consumer)

}

class Consumer extends Actor with ActorLogging{
  override def receive: Receive = withSessionLog(Map.empty)


  def withSessionLog(sessionlogMap: Map[Long, ActorRef]): Receive = {

    case r:Request =>
      log.info("Received Request")
      sessionlogMap.get(r.sessionId).fold[Unit]({

        val newSessionLogActor = context.actorOf(SessionStateLog.props)
        println("creatint new actor " + newSessionLogActor)
        newSessionLogActor ! r
        context.become(withSessionLog( sessionlogMap + (r.sessionId -> newSessionLogActor) ))
      }
      )( sessionLogActorRef => sessionLogActorRef ! r )


  }

}
