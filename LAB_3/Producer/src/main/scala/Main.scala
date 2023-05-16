import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString

object Main extends App {
  val system = ActorSystem("producer_system")
  val broker = system.actorOf(Client.props(), "message_broker")

  Thread.sleep(1000 )
  val topicMessages = Map("topic1" -> "hello from producer1 on topic1",
    "topic2" -> "hello from producer1 on topic2",
    "topic3" -> "hello from producer1 on topic3")

  //val topicsToSubscribe: Set[String] = Set("topic1", "topic2", "topic3")
  broker ! Client.PublishTopics(topicMessages)


  Thread.sleep(3000)
  // sending smth to broker as a bytestring
  //broker ! ByteString("hello from producer1 on topic1")
}
