package ActorLab.Minimal3

import akka.actor.{Actor, ActorRef, Props, Terminated}

class MonitoringActor(monitored: ActorRef) extends  Actor{
  //register itself to receive a Terminated message from
  // the actor system when the monitored actor stops.
  context.watch(monitored)

  def receive = {
    case Terminated(actor) if actor == monitored =>
      println("Monitored actor stopped!")
  }
  }


