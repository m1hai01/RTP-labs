package Week6

import Week6.TweetDb.{CreateTweet, GetTweet, GetTweets}
import akka.actor.{Actor, ActorLogging, Props}
import com.fasterxml.jackson.module.scala.DefaultScalaModule


class TweetDb extends Actor with ActorLogging {

  // Importing the necessary classes
  import com.fasterxml.jackson.databind.ObjectMapper
  import redis.clients.jedis.Jedis

  // Creating an instance of the ObjectMapper to serialize and deserialize JSON data
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  // Creating a new instance of the Jedis client, which will be used to communicate with the Redis server
  private val jedis = new Jedis("localhost", 6379)

  // Implementing the receive method, which defines how the actor responds to messages it receives
  override def receive: Receive = {

    // If the actor receives a GetTweet message with an ID, it retrieves the corresponding tweet from Redis and sends it back to the sender
    case GetTweet(id) =>
      val tweet = jedis.hget("tweets", id) // Retrieve the tweet from Redis
      sender() ! mapper.readValue(tweet, classOf[String]) // Deserialize the tweet using the ObjectMapper and send it back to the sender

    // If the actor receives a CreateTweet message with a tweet and an ID, it stores the tweet in Redis with the given ID
    case CreateTweet(tweet, id) =>
      jedis.hset("tweets", id, tweet) // Store the tweet in Redis with the given ID

    // If the actor receives a GetTweets message with a count (optional), it retrieves all tweets from Redis and sends them back to the sender
    case GetTweets(count) =>
      val tweets = jedis.hgetAll("tweets") // Retrieve all tweets from Redis
      // If the count is specified, return only that many tweets; otherwise, return all tweets
      val returnTweets = tweets.values().toArray().map(tweet => mapper.readValue(tweet.toString, classOf[String])).take(count.getOrElse(tweets.size()))
      sender() ! returnTweets // Send the tweets back to the sender
  }
}

// Companion object for the TweetDb class, containing the message case classes and a props method to create a new actor instance
object TweetDb {
  def props(): Props = Props(new TweetDb()) // Method to create a new actor instance

  // Case class for retrieving a single tweet by its ID
  case class GetTweet(id: String)

  // Case class for retrieving multiple tweets (optional count parameter specifies how many tweets to retrieve)
  case class GetTweets(count: Option[Int] = None)

  // Case class for creating a new tweet with a specified ID
  case class CreateTweet(tweet: String, id: String)
}
