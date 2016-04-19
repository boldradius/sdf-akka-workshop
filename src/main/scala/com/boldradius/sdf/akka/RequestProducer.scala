package com.boldradius.sdf.akka

import akka.actor._
import RequestProducer._

/**
 * Manages active sessions, and creates more as needed
 */
class RequestProducer(concurrentSessions: Int) extends Actor with ActorLogging with ConfigHelper {

  import context.dispatcher

  // Interval used to check for active sessions
  private val checkSessionInterval = finiteDuration("sessions.check-interval")

  // We begin by waiting for a Start signal to arrive
  def receive: Receive = stopped

  def stopped: Receive = {
    case Start(target) =>

      // Move to a different state to avoid sending to more than one target
      context.become(producing)

      // Kickstart the session checking process
      self ! CheckSessions(target)
  }

  def producing: Receive = {
    case CheckSessions(target) =>
      // Check if more sessions need to be created, and schedule the next check
      checkSessions(target)
      context.system.scheduler.scheduleOnce(checkSessionInterval, self, CheckSessions(target))

    case Stop =>
      log.info("Stopping simulation")
      context.become(stopped)
  }


  private def checkSessions(target: ActorRef) {

    // Check child actors, if not enough, create one more
    val activeSessions = context.children.size
    log.debug(s"Checking active sessions - found $activeSessions for a max of $concurrentSessions concurrent sessions")

    if(activeSessions < concurrentSessions) {
      log.info("Creating a new session")
      context.actorOf(SessionActor.props(target))
    }
  }
}


object RequestProducer {

  // Messaging protocol for the RequestProducer
  case class Start(target: ActorRef)
  case object Stop
  case class CheckSessions(target: ActorRef)

  def props(concurrentSessions: Int) =
    Props(new RequestProducer(concurrentSessions))
}

