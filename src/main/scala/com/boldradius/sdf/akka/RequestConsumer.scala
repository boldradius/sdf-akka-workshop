package com.boldradius.sdf.akka

import akka.actor._

object RequestConsumer {
  def props = Props[RequestConsumer]
}


class RequestConsumer extends Actor with ActorLogging {

  private var sessionActors = Map.empty[Long, ActorRef].withDefaultValue(context.actorOf(???))

  def receive: Receive = {
    case request @ Request(id, timestamp, url, referrer, browser) =>
      sessionActors(id) ! request
  }
}
