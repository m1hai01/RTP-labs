import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.ByteString

import scala.collection.mutable.ArrayBuffer

object Main extends App {
  val system = ActorSystem("consumer_system")
  val broker = system.actorOf(ConsumerClient.props(), "message_broker")

  Thread.sleep(1000 )
  val topics = ArrayBuffer("topic1", "topic2", "topic3")
  broker ! ByteString(topics.toString())



}
