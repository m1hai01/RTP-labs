import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Connect, Connected}
import akka.util.ByteString

import java.net.InetSocketAddress
import scala.collection.mutable.ArrayBuffer

//establishes a connection with the broker and handles the communication.
class ConsumerClient extends Actor with ActorLogging {
  implicit val system = context.system
  IO(Tcp) ! Connect(new InetSocketAddress("localhost", 9999))

  private var broker: ActorRef = _
  val messageRegex = """(\w+-\w+-\w+-\w+-\w+): (.+)""".r

  override def receive: Receive = {
    case c @ Connected(remote, local) =>
      log.info(s"Consumer client connected from $remote")
      broker = sender()
      broker ! Tcp.Register(self)
      context.become {
        case data: ByteString =>
          log.info(s"Consumer client sending data: $data")
          broker ! Tcp.Write(data)

        case Tcp.Received(data) =>
          val input = data.utf8String
          log.info(s"Received data: $input")
          input match {
            case messageRegex(_, message) =>
              log.info(s"Received message: $message ")
              broker ! Tcp.Write(ByteString(message))

            case _ =>
              log.info("Received unknown message")
          }

        case _: Tcp.ConnectionClosed =>
          log.info(s"Consumer client disconnected from $remote")
      }
  }
}

object ConsumerClient {
  def props(): Props =
    Props(new ConsumerClient())
}
