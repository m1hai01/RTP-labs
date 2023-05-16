import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App {
  val system = ActorSystem("broker_system")
  val broker = system.actorOf(Props[MessageBroker], "message_broker")
  val tcpServer = system.actorOf(Props(new TcpServer("localhost", 9999, broker)), "tcp_server")
}
