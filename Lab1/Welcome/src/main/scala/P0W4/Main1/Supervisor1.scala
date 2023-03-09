package P0W4.Main1

import P0W4.Main1.JoinerObj.JoinMessage
import P0W4.Main1.StringLowercaserAndSwapper.LowercaseAndSwap
import akka.actor.{Actor, ActorLogging, ActorRef, AllForOneStrategy, Props, SupervisorStrategy, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.concurrent.duration.DurationInt

// The Supervisor1 actor is responsible for supervising
// a set of child actors that perform some processing on incoming messages.
class Supervisor1 extends Actor with ActorLogging {
  override val supervisorStrategy: SupervisorStrategy = {
    //if one of the child actors fails, all the other child actors will be stopped and
    // restarted with the same message.
    AllForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1.minute,
      loggingEnabled = true
    )(SupervisorStrategy.defaultDecider)
  }
// route(directioneaza) incoming messages to its child actors
  private val router: Router = {
    Router(RoundRobinRoutingLogic(), routees)
  }
  //mutable sequence of child actors
  private var routees = createRoutees()
  //reference to the child actor
  private var splitterRef: ActorRef = _

  override def receive: Receive = {
    case SupervisorObject.SendMessage(message) =>
      splitterRef ! StringSplitter.SplitMessage(message)
    case Terminated(ref) =>
      log.info("Worker {} terminated and restarting", ref.path.name)

      val newRoutees = createRoutees()
      for (r <- routees) {
        router.removeRoutee(r)
      }

      for (r <- newRoutees) {
        router.addRoutee(r)
      }
  }

  private def createRoutees(): IndexedSeq[ActorRefRoutee] = {
    var result = IndexedSeq.empty[ActorRefRoutee]

    val printer = context.actorOf(StringPrinter.props(), workerName())
    val joiner = context.actorOf(JoinerObj.props(printer), workerName())
    val lowercaserAndSwapper = context.actorOf(StringLowercaserAndSwapper.props(joiner), workerName())
    val splitter = context.actorOf(StringSplitter.props(lowercaserAndSwapper), workerName())
    splitterRef = splitter

    context.watch(printer)
    context.watch(splitter)
    context.watch(lowercaserAndSwapper)
    context.watch(joiner)

    result = result :+ ActorRefRoutee(printer)
    result = result :+ ActorRefRoutee(splitter)
    result = result :+ ActorRefRoutee(lowercaserAndSwapper)
    result = result :+ ActorRefRoutee(joiner)

    result
  }

  private def workerName(): String = {
    s"worker-${java.util.UUID.randomUUID().toString}"
  }
}

object SupervisorObject {
  def props(): Props = Props(new Supervisor1())

  case class SendMessage(message: String)
}

class SplitterActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringSplitter.SplitMessage(message) =>
      if (message.contains('*')) {
        throw new IllegalArgumentException("Message is empty")
      }
      val words = message.split("\\s+")
      nextActor ! LowercaseAndSwap(words)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Restarting SplitterActor")
  }
}

object StringSplitter {
  def props(nextActor: ActorRef): Props = Props(new SplitterActor(nextActor))

  final case class SplitMessage(message: String)
}

class LowerAndSwapActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringLowercaserAndSwapper.LowercaseAndSwap(words) => {
      val lowercasedWords = words.map(_.toLowerCase)
      val swappedMsWithNs = lowercasedWords.map(str => str.map {
        case 'm' => 'n'
        case 'n' => 'm'
        case c => c
      })

      nextActor ! JoinMessage(swappedMsWithNs)
    }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("StringLowercaserAndSwapper restarted")
  }
}

object StringLowercaserAndSwapper {
  def props(nextActor: ActorRef): Props = Props(new LowerAndSwapActor(nextActor))

  final case class LowercaseAndSwap(words: Array[String])
}


class JoinerActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case JoinerObj.JoinMessage(message) =>
      val joinedWords = message.mkString(" ")
      nextActor ! StringPrinter.PrintMessage(joinedWords)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("JoinerObj restarted")
  }
}

object JoinerObj {
  def props(nextActor: ActorRef): Props = Props(new JoinerActor(nextActor))

  final case class JoinMessage(words: Array[String])
}



object StringPrinter {
  def props(): Props = Props(new PrinterActor())

  final case class PrintMessage(message: String)
}


class PrinterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringPrinter.PrintMessage(message) =>
      log.info(message)
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Restarting PrinterActor")
  }
}