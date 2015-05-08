package com.boldradius.sdf.akka

import akka.actor._
import System.{currentTimeMillis => now}
import SessionActor._

// Wraps around a session and emits requests to the target actor
class SessionActor(target: ActorRef) extends Actor with ActorLogging {

  import context.dispatcher

  // Upon actor creation, build a new session
  val session = new Session

  // This actor should only live for a certain duration, then shut itself down
  context.system.scheduler.scheduleOnce(session.duration, self, PoisonPill)

  // Start the simulation
  self ! Click

  override def receive = {
    case Click =>
      // Send a request to the target actor
      val request = session.request
      target ! request

      // Schedule a Click message to myself after some time visiting this page
      val pageDuration = Session.randomPageTime(request.url)

      log.debug(s"Scheduling another click in $pageDuration for page ${request.url}")
      context.system.scheduler.scheduleOnce(pageDuration, self, Click)
  }
}

object SessionActor {

  def props(target: ActorRef) = Props(new SessionActor(target))

  // Message protocol for the SessionActor
  case object Click
}
