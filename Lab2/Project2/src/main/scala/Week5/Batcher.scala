package Week5

import Week5.Batcher.{GetTweet, PrintSavedTweets, PrintTweets, SaveTweets, TickCancellationKey}
import Week6.TweetDb
import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Timers}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.{DurationInt, FiniteDuration}

// Define a class Batcher that extends Actor and mixes in ActorLogging and Timers traits
class Batcher(batchSize: Int, timerTime: FiniteDuration) extends Actor with ActorLogging with Timers {
  // Define a mutable queue to hold tweets temporarily
  private val tweetBatch: mutable.Queue[String] = mutable.Queue[String]()
  // Initialize the current batch size as 0
  private var currentBatchSize: Int = 0

  // Import some implicit values that are used later
  implicit val timeout: Timeout = Timeout(5.seconds)
  implicit val system: ActorSystem = context.system
  implicit val dispatcher: ExecutionContextExecutor = context.dispatcher

  // Create a child actor named tweetsDatabase using the TweetDb.props() method
  private val tweetsDatabase = context.actorOf(TweetDb.props(), "TweetsDatabase")

  // Start a timer that will repeatedly send the SaveTweets message to this actor at the specified time interval
  timers.startTimerAtFixedRate(TickCancellationKey, SaveTweets, timerTime)

  // Define the behavior of the actor
  override def receive: Receive = {
    // If a new tweet is received, process it
    case GetTweet(tweet) =>
      // If the current batch is not full yet
      if (currentBatchSize < batchSize) {
        // Add the tweet to the current batch
        currentBatchSize += 1
        tweetBatch.enqueue(tweet)
      } else {
        // If the current batch is full, save the tweets and reset the batch
        self ! SaveTweets
        timers.cancel(TickCancellationKey)
        timers.startTimerAtFixedRate(TickCancellationKey, PrintTweets, timerTime)
        tweetBatch.enqueue(tweet)
      }

    // When a SaveTweets message is received, save the tweets in the database
    case SaveTweets =>
      // Print a message indicating how many batches are being saved
      println(s"Saving ${currentBatchSize} batches!")
      // Reset the current batch size to 0
      currentBatchSize = 0
      // Dequeue and save all the tweets in the temporary tweetBatch queue
      while (tweetBatch.nonEmpty) {
        val tweet = tweetBatch.dequeue
        // Generate a GUID for the tweet using the java.util.UUID.randomUUID method
        val guid = java.util.UUID.randomUUID.toString
        // Send a CreateTweet message to the tweetsDatabase actor with the tweet and GUID as parameters
        tweetsDatabase ! TweetDb.CreateTweet(tweet, guid)
      }
      // After the tweets have been saved, send a PrintSavedTweets message to this actor to print some tweets from the database
      self ! PrintSavedTweets

    // When a PrintSavedTweets message is received, print some tweets from the database
    case PrintSavedTweets =>
      // With 50% chance, print 20 tweets from the database
      val random = scala.util.Random
      val count = random.nextInt(2)
      if (count == 1) {
        // Send a GetTweets message to the tweetsDatabase actor to retrieve some tweets
        val future = tweetsDatabase ? TweetDb.GetTweets(Some(10))
        // Wait for the future to complete and get the result as an array of strings
        val tweets = Await.result(future, timeout.duration).asInstanceOf[Array[String]]
        // Print each tweet using the actor's log method
        tweets.foreach(tweet => log.info(tweet))
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

  case object SaveTweets

  case object PrintSavedTweets
}
