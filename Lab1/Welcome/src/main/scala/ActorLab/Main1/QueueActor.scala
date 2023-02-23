package ActorLab.Main1

import akka.actor.{Actor, Props}

import scala.collection.mutable.ArrayBuffer

class QueueActor extends Actor{
  var queue = ArrayBuffer[Int]()

  override def receive: Receive = {
    case x: Int =>
      queue = queue :+ x
      println("ok ")
    case "pop" => {
      if (queue.isEmpty) {
        println("Queue is empty")
      }
      else {
        println(queue(0))
        queue.remove(0)
      }
    }
  }

}
