package Week1

import Week5.Batcher.GetTweet
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import io.circe._
import io.circe.Json
import io.circe.parser._

import scala.collection.mutable.Map
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Random

// This class represents an actor that will print tweets to console
class TextPrinter(sleepTime: FiniteDuration,
                 emotions: Map[String, Double],
                  batcher: ActorRef) extends Actor with ActorLogging {

  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  implicit val requestTimeout = akka.util.Timeout(5.second)

  private val badWords = Set(
    "arse",
    "bloody",
    "bugger",
    "cow",
    "crap",
    "damn",
    "ginger",
    "git",
    "god",
    "goddam",
    "jesus christ",
    "minger",
    "sod-off",
    "arsehole",
    "balls",
    "bint",
    "bitch",
    "bollocks",
    "bullshit",
    "feck",
    "munter",
    "pissed / pissed off",
    "shit",
    "son of a bitch",
    "tits",
    "bastard",
    "beaver",
    "beef curtains",
    "bellend",
    "bloodclaat",
    "clunge",
    "cock",
    "dick",
    "dickhead",
    "fanny",
    "flaps",
    "gash",
    "knob",
    "minge",
    "prick",
    "punani",
    "pussy",
    "snatch",
    "twat",
    "cunt",
    "fuck",
    "motherfucker",
    "bonk",
    "shag",
    "slapper",
    "tart"
  )

  private val sentimentValues: Map[String, Double] = emotions

  // Receive function of the actor which will handle messages
  override def receive: Receive = {
    // Upon receiving a PrintTweet message, the actor will print the tweet to console
    case TextPrinter.PrintTweet(tweet) =>
      // Parsing the tweet as JSON
      val text = parse(tweet).getOrElse(Json.Null)
      // Checking if the tweet is null or not
      if (text.isNull) {
        throw new Exception("TWEET IS NULL")
      }
      // Extracting the message, tweet and text properties of the tweet JSON
      val tweetMessage = getJsonProperty(text, "message")
      val tweetTweet = getJsonProperty(tweetMessage, "tweet")
      var tweetText = getJsonProperty(tweetTweet, "text").toString





      // Replace bad words with stars
      for (word <- badWords) {
        tweetText = tweetText.replaceAll(s"(?i)\\b$word\\b", "*" * word.length)
      }
      // Printing the modified tweet text to console
      //println(tweetText)

      val sentimentScore = ComputeSentimentScore(tweetText.toString())
      val engagementRatio = ComputeEngagementRatio(tweetTweet)
      //println(sentimentScore)
      //println(engagementRatio)

      batcher ! GetTweet(tweetText)

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

  private def ComputeSentimentScore(input: String): Double = {
    // calculate the mean of emotional scores of each word in the tweet
    // split the input string into words and convert to a mutable buffer
    val inputWords = input.split(' ').toBuffer
    // map each word to its sentiment value (or 0.0 if not found) and store in a new buffer
    val emotionScores = inputWords.map { word =>
      sentimentValues.getOrElse(word.toLowerCase, 0.0)
    }
    // calculate the mean of the emotion scores
    val mean = emotionScores.sum / emotionScores.length
    mean
  }

  private def ComputeEngagementRatio(tweet: Json): String = {
    // get the user object from the tweet JSON
    val tweetUser = getJsonProperty(tweet, "user")

    // get the favourites_count, followers_count, and retweet_count properties from the tweet and user objects
    val tweetFavourites = getJsonProperty(tweetUser, "favourites_count")
    val tweetFollowers = getJsonProperty(tweetUser, "followers_count")
    val tweetRetweeted = getJsonProperty(tweet, "retweet_count")

    // if the user has no followers, return 0 as the engagement ratio to avoid a divide-by-zero error
    if (tweetFollowers.toString().toInt == 0) {
      return "0"
    }

    // calculate the engagement ratio as the sum of favourites and retweets divided by followers
    val engagement_ratio = (tweetFavourites.toString().toInt + tweetRetweeted.toString().toInt) / tweetFollowers.toString().toInt
    // convert the engagement ratio to a string and return it
    engagement_ratio.toString()
  }


}


// A companion object to the TextPrinter class that provides a factory method for creating TextPrinter actor props
object TextPrinter {
  def props(sleepTime: FiniteDuration,
            emotions: Map[String, Double],
            batcher: ActorRef): Props = Props(new TextPrinter(sleepTime, emotions, batcher))
  // A case class that represents a PrintTweet message containing the tweet as a String
  case class PrintTweet(tweet: String)
}
