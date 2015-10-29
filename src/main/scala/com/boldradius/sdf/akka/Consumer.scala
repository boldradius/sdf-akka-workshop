package com.boldradius.sdf.akka

import akka.actor.{Props, Actor, ActorLogging}


object Consumer {

  def props:Props = Props(new Consumer)

}

class Consumer extends Actor with ActorLogging{
  override def receive: Receive = {
    case r:Request =>
      println("************************************** ")
      log.info("Received Request")
    case x =>  log.info("Received " + x)

  }
}
