package Week2

import Week1.TextPrinter
import Week2.WorkerPool.{ActorAdd, ActorRemove, ActorsReceive, PrintTweet}
import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router, SmallestMailboxRoutingLogic}

class WorkerPool extends Actor with ActorLogging {
  // initialize the router with the SmallestMailboxRoutingLogic
  private var router: Router = {
    Router(SmallestMailboxRoutingLogic())
    // This routing logic selects the actor with the fewest messages in its mailbox.
  }

  override def receive: Receive = {
    case PrintTweet(tweet) =>
      // route the PrintTweet message to one of the routees in the router
      router.route(TextPrinter.PrintTweet(tweet), sender())

    case ActorsReceive(routees) =>
      // receive a message containing a new list of routees
      log.info("Received actors")
      // update the router with the new routees using the RoundRobinRoutingLogic
      router = Router(RoundRobinRoutingLogic(), routees)

    case ActorRemove(routee) =>
      // receive a message to remove a routee from the router
      log.info("Killed actor")
      // remove the specified routee from the router
      router.removeRoutee(routee)

    case ActorAdd(routee) =>
      // receive a message to add a new routee to the router
      log.info("Added actor")
      // watch the new routee for termination and add it to the router
      context.watch(routee.ref)
      router.addRoutee(routee)
  }
}

object WorkerPool {
  def props(): Props = Props(new WorkerPool())
  case class PrintTweet(tweet: String)
  case class ActorsReceive(routees: IndexedSeq[ActorRefRoutee])
  case class ActorRemove(routee: ActorRefRoutee)
  case class ActorAdd(routee: ActorRefRoutee)
}
