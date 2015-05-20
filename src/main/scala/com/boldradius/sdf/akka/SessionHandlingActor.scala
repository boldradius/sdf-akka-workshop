package com.boldradius.sdf.akka

import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor.{FSM, Props, ActorLogging, Actor}
import com.boldradius.sdf.akka.SessionHandlingActor._

import scala.concurrent.duration.FiniteDuration


class SessionHandlingActor(id: Long) extends FSM[SessionState, SessionData] with ActorLogging {

  startWith(Active, Requests(List.empty[Request]))

  initialize()

  when(Active) {
    case Event(request: Request, requests: Requests) =>
      setTimer("timeout", InactiveSession(id), FiniteDuration(10, TimeUnit.MILLISECONDS))
      stay() using requests.copy(list = request :: requests.list)

    case Event(InactiveSession(_), _) =>
      context.parent ! InactiveSession(id)
      goto(Inactive)
  }

  when(Inactive) {
    case Event(request: Request, _) =>
      context.parent forward request
      stay()
  }

}

object SessionHandlingActor {
  def props(id: Long): Props = Props(new SessionHandlingActor(id))

  sealed trait SessionState
  case object Active extends SessionState
  case object Inactive extends SessionState

  case class InactiveSession(id: Long)

  sealed trait SessionData
  case class Requests(list: List[Request]) extends SessionData
}
