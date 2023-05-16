import Client.PublishTopics
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Connect, Connected}
import akka.util.ByteString

import java.net.InetSocketAddress

class Client extends Actor with ActorLogging {
  implicit val system = context.system
  IO(Tcp) ! Connect(new InetSocketAddress("localhost", 9999))

  private var broker: ActorRef = _

  override def receive: Receive = {
    case c @ Connected(remote, local) =>
      log.info(s"Client connected from $remote")
      broker = sender()
      broker ! Tcp.Register(self)
      context.become {
        case Tcp.Received(data) =>
          val input = data.utf8String
          log.info(s"Received data: $input")
        case PublishTopics(topicMessages) =>
          val message = s"PUBLISH($topicMessages)"
          log.info(s"Sending message: $message")
          broker ! Tcp.Write(ByteString(message))
          log.info(s"Published topics: $topicMessages")
        case _: Tcp.ConnectionClosed =>
          log.info(s"Client from $remote disconnected")
      }
  }
}

object Client {
  def props(): Props = Props(new Client())

  case class PublishTopics(topicMessages: Map[String, String])
}
