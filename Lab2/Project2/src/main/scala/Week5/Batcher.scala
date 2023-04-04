package Week5

import Week5.Batcher.{GetTweet, PrintTweets, TickCancellationKey}
import akka.actor.{Actor, ActorLogging, Props}

import scala.collection.mutable
import akka.actor.Timers

import scala.concurrent.duration.FiniteDuration

class Batcher(batchSize: Int,
              timerTime: FiniteDuration) extends Actor with ActorLogging with Timers {
  // set mutable queue to hold tweets temporarily
  private val tweetBatch: mutable.Queue[String] = mutable.Queue[String]()
  // initialize the current batch size as 0
  private var currentBatchSize: Int = 0
  // start a timer to print tweets at a fixed rate
  timers.startTimerAtFixedRate(TickCancellationKey, PrintTweets, timerTime)

  // define the behavior of the actor
  override def receive: Receive = {
    // if a new tweet is received
    case GetTweet(tweet) =>
      // if the current batch is not full yet
      if (currentBatchSize < batchSize) {
        // add the tweet to the current batch
        currentBatchSize += 1
        tweetBatch.enqueue(tweet)
      } else {
        // if the current batch is full, print the tweets and reset the batch
        self ! PrintTweets
        timers.cancel(TickCancellationKey)
        timers.startTimerAtFixedRate(TickCancellationKey, PrintTweets, timerTime)
        tweetBatch.enqueue(tweet)
      }

    case PrintTweets =>
      // print the size of the current batch
      println(s"Printing ${currentBatchSize} batches!")
      // reset the current batch size to 0
      currentBatchSize = 0
      // dequeue tweets from the batch and log them
      while (tweetBatch.nonEmpty) {
        val tweet = tweetBatch.dequeue
        log.info(s"Tweet: ${tweet}")
      }
  }
}

// define the companion object for the Batcher actor
object Batcher {
  // create props for the actor with batch size and timer time as input parameters
  def props(batchSize: Int, timerTime: FiniteDuration): Props = Props(new Batcher(batchSize, timerTime))
  // define message to get tweets
  case class GetTweet(tweet: String)
  // define message to print tweets
  case object PrintTweets
  // define timer key for cancelling and restarting the timer
  case object TickCancellationKey
}
