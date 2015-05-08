package com.boldradius.sdf.akka

import akka.actor._

object DummyRequestConsumer {
  def props = Props[DummyRequestConsumer]
}

// Mr Dummy Consumer simply shouts to the log the messages it receives
class DummyRequestConsumer extends Actor with ActorLogging {

  def receive: Receive = {
    case message => log.debug(s"Received the following message: $message")
  }
}
