package com.boldradius.sdf.akka

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.boldradius.sdf.akka.RequestProducer._

object RequestSimulationExampleApp extends App {

  // First, we create an actor system, a producer and a consumer
  val system = ActorSystem("EventProducerExample")
  val concurrentSessions = system.settings.config.getInt("sessions.concurrent")
  val producer = system.actorOf(RequestProducer.props(concurrentSessions), "producerActor")

  // TODO: replace the Dummy request consumer with your own consumer actor
  val consumer = system.actorOf(Props(new DummyRequestConsumer))

  // Tell the producer to start working and to send messages to the consumer
  producer ! Start(consumer)

  // Wait for the user to hit <enter>
  println("Hit <enter> to stop the simulation")
  StdIn.readLine()

  // Tell the producer to stop working
  producer ! Stop

  // Terminate all actors and wait for graceful shutdown
  system.terminate()
  Await.result(system.whenTerminated, 1 minute)
}

class DummyRequestConsumer extends Actor with ActorLogging {
  override def receive: Receive = {
    case message => log.info(s"Received the following message: $message")
  }
}
