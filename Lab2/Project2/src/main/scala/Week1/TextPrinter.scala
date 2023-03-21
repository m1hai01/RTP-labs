package Week1

// Importing necessary libraries and classes
import akka.actor.{Actor, ActorLogging, Props}
import io.circe._
import io.circe.parser._

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

// This class represents an actor that will print tweets to console
class TextPrinter(sleepTime: FiniteDuration) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  implicit val requestTimeout = akka.util.Timeout(5.second)

  // Receive function of the actor which will handle messages
  override def receive: Receive = {
    // Upon receiving a PrintTweet message, the actor will print the tweet to console
    case TextPrinter.PrintTweet(tweet) =>
      // Parsing the tweet as JSON
      val text = parse(tweet).getOrElse(Json.Null)
      // Checking if the tweet is null or not
      //json dont parse
      if (text.isNull) {
        throw new Exception("TWEET IS NULL")
      }
      // Extracting the message, tweet and text properties of the tweet JSON
      val tweetMessage = getJsonProperty(text, "message")
      val tweetTweet = getJsonProperty(tweetMessage, "tweet")
      val tweetText = getJsonProperty(tweetTweet, "text")
      // Printing the tweet text to console
      println(tweetText)
      // Sleeping for a random time between 10-50 milliseconds + the specified sleep time
      Thread.sleep(Random.between(10, 50) + sleepTime.toMillis)
  }

  // A utility function that extracts a JSON property from a JSON object
  private def getJsonProperty(json: Json, property: String): Json = {
    json
      .asObject
      .get(property)
      .getOrElse(Json.Null)
  }
}

// A companion object to the TextPrinter class that provides a factory method for creating TextPrinter actor props
object TextPrinter {
  def props(sleepTime: FiniteDuration): Props = Props(new TextPrinter(sleepTime: FiniteDuration))
  // A case class that represents a PrintTweet message containing the tweet as a String
  case class PrintTweet(tweet: String)
}