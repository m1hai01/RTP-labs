package Week2

import Week1.TextPrinter
import akka.actor.{Actor, ActorLogging, ActorRef, Props, SupervisorStrategy, Terminated}
import akka.pattern.ask
import akka.routing.ActorRefRoutee
import scala.collection.mutable.Map
import scala.concurrent.duration.DurationInt

class PoolSupervisor(workerPool: ActorRef,
                    workersCount: Int,
                     emotions: Map[String, Double]) extends Actor with ActorLogging {

  // Use the default supervisor strategy for actors under this supervisor
  override val supervisorStrategy: SupervisorStrategy =
    SupervisorStrategy.defaultStrategy

  private val sentimentValues = emotions

  // Define the pause duration for each worker actor and the number of workers
  private val pauseDuration = 30.milliseconds
  private val nrOfActors = 3

  // Create a sequence of worker actors as routees, and register them for termination events
  private var routees = IndexedSeq.fill(nrOfActors) {
    val name = workerName()
    val r = context.actorOf(TextPrinter.props(pauseDuration, sentimentValues), name)
    context.watch(r) // Register for termination events
    ActorRefRoutee(r)
  }

  // Notify the worker pool about the initial set of actors
  workerPool ! WorkerPool.ActorsReceive(routees)

  // Handle termination events of worker actors
  override def receive: Receive = {
    case Terminated(ref) =>
      log.info("Worker {} terminated, restarting...", ref.path.name)

      // Get the dead worker actor
      val deadTextPrinter = ref.actorRef

      // Remove the dead worker actor from the routees list
      val index = routees.indexOf(deadTextPrinter)
      routees = routees.patch(index, Nil, 1)

      // Create a new worker actor to replace the dead one, and register it for termination events
      val name = workerName()
      //val newTweetPrinter = context.actorOf(TextPrinter.props(pauseDuration), name)
      val newTweetPrinter = context.actorOf(TextPrinter.props(pauseDuration, sentimentValues), name)
      context.watch(newTweetPrinter)

      // Notify the worker pool about the dead and new actors
      workerPool ! WorkerPool.ActorRemove(ActorRefRoutee(deadTextPrinter))
      workerPool ! WorkerPool.ActorAdd(ActorRefRoutee(newTweetPrinter))
  }

  // Generate a unique name for each worker actor
  private def workerName(): String = {
    s"tweetPrinter-${java.util.UUID.randomUUID().toString}"
  }
}

object PoolSupervisor {
  def props(workerPool: ActorRef,
            workersCount: Int,
            emotions: Map[String, Double]): Props = Props(new PoolSupervisor(workerPool,workersCount, emotions))
}
