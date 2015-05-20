package com.boldradius.sdf.akka

import akka.actor._
import com.boldradius.sdf.akka.SessionHandlingActor.InactiveSession

object RequestConsumer {
  def props = Props[RequestConsumer]
}

class RequestConsumer extends Actor with ActorLogging {

  private var sessionHandlers = Map.empty[Long, ActorRef]

  def receive: Receive = {
    case request: Request => handleRequest(request)

    case InactiveSession(id) =>
      context.stop(sessionHandlers(id))
      sessionHandlers -= id
  }

  private def handleRequest(request: Request) = {
    if (sessionHandlers.contains(request.sessionId)) {
      sessionHandlers(request.sessionId) ! request
    } else {
      val sessionHandler = context.actorOf(SessionHandlingActor.props(request.sessionId))
      sessionHandlers += (request.sessionId -> sessionHandler)
      sessionHandler ! request
    }
  }
}
