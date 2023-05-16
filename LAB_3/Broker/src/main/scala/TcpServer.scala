import java.net.InetSocketAddress
import akka.io._
import akka.io.Tcp._
import akka.util.ByteString
import akka.actor._
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

class TcpServer(host: String, port: Int, broker: ActorRef) extends Actor {
  import context.system

  // Bind to the specified host and port
  IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))

  def receive = {
    case Bound(localAddress) =>
      println(s"TCP server bound to $localAddress")
    case CommandFailed(_: Bind) =>
      println(s"TCP server bind failed")
      context.stop(self)
    case Connected(remote, local) =>
      // Create a new ClientHandler actor for each client connection
      val handler = context.actorOf(Props(new ClientHandler(remote, broker)))
      val connection = sender()
      connection ! Register(handler)
  }
}

class ClientHandler(remote: InetSocketAddress, broker: ActorRef) extends Actor {
  import Tcp._
  import MessageBroker._

  println(s"Client connected from $remote")

  // Regular expressions for matching subscribe and publish patterns
  private val publishPattern = """PUBLISH\(Map\((.+)\)\)""".r
  val subscribe: Regex = """ArrayBuffer\(([\w\s,]+)\)""".r

  def receive = {
    case Received(data) =>
      val input = data.utf8String
      println(s"Received data: $input")

      input match {
        // For subscribe commands, it extracts the topics and
        // subscribes the client to each topic.
        case subscribe(matchResult) =>
          // Extract topics from the match result
          val topics = splitTopicString(matchResult)
          val topicsBuffer = ArrayBuffer[String]()
          for (topic <- topics) {
            topicsBuffer += topic
          }
          val subscriber = sender()
          println(s"Subscribing to $topicsBuffer")
          // Assign the subscriber to each topic
          for (topic <- topicsBuffer) {
            broker ! MessageBroker.Subscribe(topic, subscriber)
          }

          //For publish commands, it extracts the topic-message pairs and
        // sends them to the broker for publishing.
        case publishPattern(topicMessagePairs) =>
          // Extract topic and message pairs from the input
          val topicMessageRegex = """(\w+)\s*->\s*([^,]+)""".r
          val topicMessageMatches = topicMessageRegex.findAllMatchIn(topicMessagePairs)
          topicMessageMatches.foreach { matchResult =>
            val topic = matchResult.group(1)
            val message = matchResult.group(2)
            println(s"Topic: $topic, Message: $message")
            broker ! Publish(topic, message)
          }

        case _ =>
          println("Invalid input format")
      }

    case PeerClosed =>
      println(s"Client from $remote disconnected")
      context.stop(self)
  }

  // Utility method to split the topic string into individual topics
  private def splitTopicString(topic: String) = {
    topic.split(",").map(_.trim)
  }

  override def postStop() = {
    println(s"Client from $remote disconnected")
  }
}
