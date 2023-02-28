package P0W4.Minimal1

import P0W4.Minimal1.PrintActorObject._
import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy, Terminated}
import akka.pattern.ask
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
class PrintActorClass(name: String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Print(message) =>
      log.info("Worker Actor {} received message: {}", name, message)
      sender() ! s"Worker Actor $name print: $message"
    case Kill =>
      log.warning("Worker Actor {} received Kill ", name)
      throw new Exception(s"Worker Actor $name is killed")
  }

  override def postStop(): Unit = {
    log.info("Worker Actor {} is  stopped", name)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Actor Worker {} is restarting", name)
  }

}
object SupervisorActor {
  def props(): Props = Props(new SupervisorClass())

  case class SendMessage(message: String)

  case object SendWorkers
}

class SupervisorClass extends Actor with ActorLogging {

  import SupervisorActor._
//The defaultStrategy is used, which means that
// the supervisor will restart the worker actor
// if it throws an exception.
  override val supervisorStrategy: SupervisorStrategy =
    SupervisorStrategy.defaultStrategy

  //distribute messages among a group of actors.
  //what logic of transmiting messages
  private val router: Router = {
    Router(RoundRobinRoutingLogic(), routees)
  }
  private val workerCount = 3
  //the group of worker actors
  // that the supervisor is responsible for supervising
  private var routees = IndexedSeq.fill(workerCount) {
    val name = workerName()
    // Each worker actor is created
    val r = context.actorOf(PrintActorObject.props(name), name)
    context.watch(r)
    ActorRefRoutee(r)
  }

  println(routees)

  override def receive: Receive = {
    case SendMessage(message) =>
      log.info("Sending messages to all workers: {}", message)
      router.route(PrintActorObject.Print(message), sender())
      //transmit the list of references of workes back to the sender
    case SendWorkers =>
      log.info("Sending workers")
      sender() ! routees
    case Terminated(ref) =>
      log.info("Worker {} terminated, restarting...", ref.path.name)

      val index = routees.indexOf(ref.actorRef)
      routees = routees.patch(index, Nil, 1)

      val name = workerName()
      val r = context.actorOf(PrintActorObject.props(name), name)
      context.watch(r)
      router.removeRoutee(ref)
      router.addRoutee(ActorRefRoutee(r))
  }

  private def workerName(): String = {
    s"worker-${java.util.UUID.randomUUID().toString}"
  }
}

object PrintActorObject {
  def props(name: String): Props = Props(new PrintActorClass(name))

  case class Print(message: String)

  case object Kill
}
