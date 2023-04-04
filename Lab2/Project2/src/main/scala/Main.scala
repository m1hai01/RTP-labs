import Week1.{StreamReader, TextPrinter}
import Week2.{PoolSupervisor, WorkerPool}
import Week4.SentimentReader
import Week4.SentimentReader.Send
import Week5.Batcher
import akka.actor
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


object Main {
  def main(args: Array[String]): Unit = {
    val system = actor.ActorSystem("StreamReaderSystem")

    //val tweets1 = "http://localhost:4000/tweets/1"
    //val tweets2 = "http://localhost:4000/tweets/2"

    val localHost = "http://localhost:4000"
    val tweets1 = s"${localHost}/tweets/1"
    val tweets2 = s"${localHost}/tweets/2"
    val sentimentValues = s"${localHost}/emotion_values"

    val sentimentReader = system.actorOf(SentimentReader.props(sentimentValues), "SentimentReader")

    implicit val timeout: Timeout = Timeout(5.seconds)
    val future = sentimentReader ? Send
    val emotions = Await.result(future, timeout.duration).asInstanceOf[mutable.Map[String, Double]]
    val workersCount = 3
    val batchSize = 30
    val timerTime = 1.seconds


    val batcher = system.actorOf(Batcher.props(batchSize, timerTime), "TweetBatcher")

    val workerPool = system.actorOf(WorkerPool.props, "WorkerPool")
    val poolSupervisor = system.actorOf(PoolSupervisor.props(workerPool, workersCount, emotions, batcher), "PoolSupervisor")

    val streamReader = system.actorOf(StreamReader.props(tweets1, workerPool), "StreamReader")
    val streamReader2 = system.actorOf(StreamReader.props(tweets2, workerPool), "StreamReader2")
    streamReader ! StreamReader.Send
    streamReader2 ! StreamReader.Send


  }
}