package com.boldradius.sdf.akka

import akka.actor.ActorSystem
import com.boldradius.sdf.akka.RequestProducer._
import scala.concurrent.Await
import scala.io.StdIn
import scala.concurrent.duration._

object RequestSimulationExampleApp extends App {

  // First, we create an actor system, a producer and a consumer
  val system = ActorSystem("EventProducerExample")
  val concurrentSessions = system.settings.config.getInt("sessions.concurrent")
  val producer = system.actorOf(RequestProducer.props(concurrentSessions), "producerActor")

  // TODO: replace dead letters with your own consumer actor
  val consumer = system.deadLetters

  // Tell the producer to start working and to send messages to the consumer
  producer ! Start(system.actorOf(akka.actor.Props(new LogActor)))

  // Wait for the user to hit <enter>
  println("Hit <enter> to stop the simulation")
  StdIn.readLine()

  // Tell the producer to stop working
  producer ! Stop

  // Terminate all actors and wait for graceful shutdown
  system.terminate()
  Await.result(system.whenTerminated, 1 minute)
}

class LogActor extends akka.actor.Actor with akka.actor.ActorLogging{
  override def receive = {
    case m => log.info(s"Received message: '$m'")
  }
}
