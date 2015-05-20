package com.boldradius.sdf.akka

import akka.actor._

object RequestConsumer {
  def props = Props[RequestConsumer]
}


class RequestConsumer extends Actor with ActorLogging {

  private var sessionHandlers = Map.empty[Long, ActorRef]

  def receive: Receive = {
    case request: Request => handleRequest(request)
  }

  private def handleRequest(request: Request) = {
    if (sessionHandlers.contains(request.sessionId)) {
      sessionHandlers(request.sessionId) ! request
    } else {
      val sessionHandler = context.actorOf(SessionHandlingActor.props)
      sessionHandlers += (request.sessionId -> sessionHandler)
      sessionHandler ! request
    }
  }
}
