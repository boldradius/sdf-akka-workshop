package com.boldradius.sdf.akka

import akka.actor.ActorSystem
import com.boldradius.sdf.akka.RequestProducer._
import scala.io.StdIn
import scala.concurrent.duration._

object RequestSimulationExampleApp extends App {

  // First, we create an actor system, a producer and a consumer
  val system = ActorSystem("EventProducerExample")
  val producer = system.actorOf(RequestProducer.props(100), "producerActor")
  val consumer = system.actorOf(RequestConsumer.props, "dummyConsumer")

  // Tell the producer to start working and to send messages to the consumer
  producer ! Start(consumer)

  // Wait for the user to hit <enter>
  println("Hit <enter> to stop the simulation")
  StdIn.readLine()

  // Tell the producer to stop working
  producer ! Stop

  // Terminate all actors and wait for graceful shutdown
  system.shutdown()
  system.awaitTermination(10 seconds)
}
