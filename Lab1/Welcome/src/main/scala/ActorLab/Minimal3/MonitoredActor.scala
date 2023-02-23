package ActorLab.Minimal3

import akka.actor.Actor

class MonitoredActor extends Actor{
  def receive = {
    case msg: String => println(s"Received message: $msg")
  }
}
