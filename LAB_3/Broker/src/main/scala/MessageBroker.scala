import akka.actor._
import akka.actor.Actor
import akka.io.Tcp
import akka.util.ByteString

import scala.collection.mutable
import scala.util.matching.Regex

class MessageBroker extends Actor {
  import MessageBroker._

  println("Message broker started")

  // Map to store topic subscriptions
  var subscriptions: Map[String, Set[ActorRef]] = Map.empty
  private var broker: ActorRef = _

  def receive = {
    case Subscribe(topic, subscriber) =>
      // Add the subscriber to the set of subscribers for the given topic
      if (subscriptions.contains(topic)) {
        subscriptions += (topic -> (subscriptions(topic) + subscriber))
      } else {
        subscriptions += (topic -> Set(subscriber))
      }
      println(s"$subscriber subscribed to $topic")

      //When a message is published to a topic,
    // the broker retrieves the subscribers for that topic and sends the message to each subscriber.
    case Publish(topic, message) =>
      // Publish the message to subscribers of the given topic
      println(s"Publishing '$message' on $topic")

      subscriptions.get(topic) match {
        case Some(subscribers) =>
          subscribers.foreach(_ ! Tcp.Write(ByteString(s"$message\n")))
          println(s"Published '$message' on $topic")
        case None =>
          println(s"Topic $topic does not exist")
      }
  }

  override def postStop() = {
    println("Message broker stopped")
  }
}

object MessageBroker {
  case class Subscribe(topic: String, subscriber: ActorRef)
  case class Unsubscribe(topic: String)
  case class Publish(topic: String, message: String)
  case class Message(topic: String, message: String)
  case class SubscribeAll(topics: Set[String], subscriber: ActorRef)
  case class UnsubscribeAll(topics: Set[String], subscriber: ActorRef)

  def props: Props = Props[MessageBroker]
}
