package ActorLab.Minimal2

import akka.actor.Actor

class ModifierActor extends Actor {
  def receive = {
    case i: Int =>
      val modified = i + 1
      println(s"Received: $i, Modified: $modified")
      sender() ! modified

    case s: String =>
      val modified = s.toLowerCase
      println(s"Received: $s, Modified: $modified")
      sender() ! modified

    case _ =>
      val modified = "I don't know how to HANDLE this!"
      println(s"Received unknown message, Modified: $modified")
      sender() ! modified
  }

}
