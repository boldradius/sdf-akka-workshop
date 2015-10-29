package com.boldradius.sdf.akka

import akka.actor.{ActorLogging, Props, Actor, ActorSystem}
import com.boldradius.sdf.akka.RequestProducer._
import com.boldradius.sdf.akka.RequestSimulationExampleApp.StartApp
import scala.io.StdIn
import scala.concurrent.duration._

object  RequestSimulationExampleApp extends App {

  case object StopApp
  case object StartApp


  // First, we create an actor system, a producer and a consumer
  val system = ActorSystem("EventProducerExample")


  val simulationApp = system.actorOf(Props(new RequestSimulationExampleApp))

  // Tell the producer to start working and to send messages to the consumer
  simulationApp ! StartApp



  // Wait for the user to hit <enter>
  println("Hit <enter> to stop the simulation")
  StdIn.readLine()


  system.stop(simulationApp)

  // Terminate all actors and wait for graceful shutdown
  system.shutdown()
  system.awaitTermination(10 seconds)
}


class RequestSimulationExampleApp extends Actor with ActorLogging{


  val inactiveTimeout:FiniteDuration = FiniteDuration(context.system.settings.config.getDuration("session.inactive", SECONDS),SECONDS)




  val producer = context.system.actorOf(RequestProducer.props(100), "producerActor")

  val consumer = context.system.actorOf(Consumer.props(inactiveTimeout), "consumerActor")

  val statsActor = context.system.actorOf(Stats.props, "statsActor")


//  // Tell the producer to start working and to send messages to the consumer
//  producer ! Start(consumer)


  def receive: Receive = {

    case msg:Stats.SessionStats => statsActor.forward(msg)

    case StartApp =>  producer ! Start(consumer)

  }

//
//  def stop() = {
//    // Tell the producer to stop working
//    producer ! Stop
//
//  }
//

}

