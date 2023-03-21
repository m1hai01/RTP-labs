import Week1.{StreamReader, TextPrinter}
import Week2.{PoolSupervisor, WorkerPool}
import akka.actor

import scala.concurrent.duration.DurationInt

object Main {
  def main(args: Array[String]): Unit = {
    val system = actor.ActorSystem("StreamReaderSystem")

    val tweets1 = "http://localhost:4000/tweets/1"
    val tweets2 = "http://localhost:4000/tweets/2"

    val textPrinter = system.actorOf(TextPrinter.props(30.milliseconds), "TextPrinter")

    val workerPool = system.actorOf(WorkerPool.props, "WorkerPool")
    val poolSupervisor = system.actorOf(PoolSupervisor.props(workerPool), "PoolSupervisor")

    val streamReader = system.actorOf(StreamReader.props(tweets1, workerPool), "StreamReader")
    val streamReader2 = system.actorOf(StreamReader.props(tweets2, workerPool), "StreamReader2")
    streamReader ! StreamReader.Send
    streamReader2 ! StreamReader.Send


  }
}