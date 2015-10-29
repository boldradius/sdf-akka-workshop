package com.boldradius.sdf.akka

import akka.actor.{Actor, ActorSystem}
import com.boldradius.sdf.akka.RequestProducer._
import scala.io.StdIn
import scala.concurrent.duration._

object  RequestSimulationExampleApp extends App {

  // First, we create an actor system, a producer and a consumer
  val system = ActorSystem("EventProducerExample")


  val simulationApp = new RequestSimulationExampleApp(system)

  // Wait for the user to hit <enter>
  println("Hit <enter> to stop the simulation")
  StdIn.readLine()


  simulationApp.stop()

  // Terminate all actors and wait for graceful shutdown
  system.shutdown()
  system.awaitTermination(10 seconds)
}


class RequestSimulationExampleApp(system:ActorSystem){

  val producer = system.actorOf(RequestProducer.props(100), "producerActor")

  // TODO: replace dead letters with your own consumer actor
  val consumer = system.actorOf(Consumer.props, "consumerActor")

  // Tell the producer to start working and to send messages to the consumer
  producer ! Start(consumer)


  def stop() = {
    // Tell the producer to stop working
    producer ! Stop

  }


}

