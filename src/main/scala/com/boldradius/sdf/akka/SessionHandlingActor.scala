package com.boldradius.sdf.akka

import akka.actor.{Props, ActorLogging, Actor}


class SessionHandlingActor extends Actor with ActorLogging {

  private var listRequest: List[Request] = List()

  override def receive: Receive = {
    case request: Request =>
      listRequest = request :: listRequest
  }
}

object SessionHandlingActor {
  def props: Props = Props[SessionHandlingActor]
}
