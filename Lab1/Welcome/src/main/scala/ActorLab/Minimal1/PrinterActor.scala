package ActorLab.Minimal1

import akka.actor.Actor

class PrinterActor extends Actor {
  def receive = {
    case message: Any =>
      println(message)
  }
}
