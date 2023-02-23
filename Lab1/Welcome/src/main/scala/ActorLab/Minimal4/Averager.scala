package ActorLab.Minimal4

import akka.actor.Actor
import akka.event.Logging

class Averager extends Actor{
  //val log = Logging(context.system, this)
  var sum: Double = 0
  var avarage: Double = 0

  def receive = {
    case x: Int =>
      sum = avarage + x
      avarage = sum / 2
      println(s"Current average is $avarage")
      //log.info(s"Current average is $avarage")
      sender() ! s"Current average is $avarage"
  }
}
