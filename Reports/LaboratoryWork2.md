# FAF.PTR16.1 -- Project 1

> **Performed by:** Mustuc Mihai, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P1W1

**Task 1 (Minimal Task)** -- Initialize a VCS repository for your project.

**Task 2 (Minimal Task)** -- Write an actor that would read SSE streams. The SSE streams for this lab are available on Docker Hub at alexburlacu/rtp-server, courtesy of our beloved FAFer Alex Burlacu.

```scala
   override def receive: Receive = {
    case Send =>

      // Define a function that sends an HTTP request and returns a future HTTP response
      val request: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)

      // Create an event source that connects to the specified URI and sends requests using the defined function
      val eventSource = EventSource(
        uri = uriAddress,
        send = request,
        initialLastEventId = None,
        retryDelay = 1.second
      )

      // Continuously read and process events from the stream
      while (true) {
        
        // Process the events in the response by sending each tweet's text to the TextPrinter actor
        responseFuture.foreach(serverSentEvent => serverSentEvent.foreach(
          event => {
            val tweetData = event.getData()
            //textPrinter ! TextPrinter.PrintTweet(tweetData)
            tweetRouter ! WorkerPool.PrintTweet(tweetData)

          }
        ))

        // Block the thread until the response future is completed to prevent busy waiting
        while (!responseFuture.isCompleted) {}
      }
  }

```



**Task 3 (Minimal Task)** -- Create an actor that would print on the screen the tweets it receives from the SSE Reader. You can only print the text of the tweet to save on screen space.

```scala
  override def receive: Receive = {
    // Upon receiving a PrintTweet message, the actor will print the tweet to console
    case TextPrinter.PrintTweet(tweet) =>
      // Parsing the tweet as JSON
      val text = parse(tweet).getOrElse(Json.Null)
      }
      // Extracting the message, tweet and text properties of the tweet JSON
      val tweetMessage = getJsonProperty(text, "message")
      val tweetTweet = getJsonProperty(tweetMessage, "tweet")
      var tweetText = getJsonProperty(tweetTweet, "text").toString
  

    private def getJsonProperty(json: Json, property: String): Json = {
    json
      .asObject
      .get(property)
      .getOrElse(Json.Null)
  }
```
The main functionality of this code is to parse a tweet message (presumably in JSON format) and extract the text content of the tweet. The parse method is called to parse the incoming tweet message, which returns an instance of Json, an abstract data type representing JSON values. The getJsonProperty method is then called to extract the message, tweet, and text properties of the tweet JSON.

**Task 4 (Main Task)** -- Create a second Reader actor that will consume the second stream provided by the Docker image. Send the tweets to the same Printer actor.

```scala
    val streamReader = system.actorOf(StreamReader.props(tweets1, textPrinter), "StreamReader")
    val streamReader2 = system.actorOf(StreamReader.props(tweets2, textPrinter), "StreamReader2")
    streamReader ! StreamReader.Send
    streamReader2 ! StreamReader.Send

```
The main functionality of this code creates two instances of an actor called StreamReader and sends them a Send message, presumably triggering them to start processing tweet data. It is difficult to say exactly what the StreamReader actor does or how it fits into a larger system without more context.

**Task 5 (Main Task)** -- Continue your Printer actor. Simulate some load on the actor by sleeping every time a tweet is received. Suggested time of sleep – 5ms to 50ms. Consider using Poisson distribution. Sleep values / distribution parameters need to be parameterizable.

 ```scala
       // Sleeping for a random time between 10-50 milliseconds + the specified sleep time
      Thread.sleep(Random.between(10, 50) + sleepTime.toMillis)
```
This code causes the current thread to sleep for a random duration of time between 10-50 milliseconds, plus a specified sleep time represented by the sleepTime variable, which is assumed to be an instance of java.time.Duration. It is likely used to introduce a random delay between processing tasks in a larger system.


## P1W2

**Task 1 (Minimal Task)** -- Create a Worker Pool to substitute the Printer actor from previous week. The pool will contain 3 copies of the Printer actor which will be supervised by a Pool Supervisor. Use the one-for-one restart policy.


```scala
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

  }
```
This code defines an actor called WorkerPool that receives a PrintTweet message and routes it to one of several routees using a router initialized with SmallestMailboxRoutingLogic(). The SmallestMailboxRoutingLogic() routing logic selects the actor with the fewest messages in its mailbox. The route method is used to pass the message to be routed, and the sender of the message, to the router. The exact functionality of the PrintTweet message and the routees is not shown.

```scala
class PoolSupervisor(workerPool: ActorRef,
                    workersCount: Int,
                     emotions: Map[String, Double]) extends Actor with ActorLogging {

  // Use the default supervisor strategy for actors under this supervisor
  override val supervisorStrategy: SupervisorStrategy =
    SupervisorStrategy.defaultStrategy

  // Create a sequence of worker actors as routees, and register them for termination events
  private var routees = IndexedSeq.fill(nrOfActors) {
    val name = workerName()
    val r = context.actorOf(TextPrinter.props(pauseDuration, sentimentValues), name)
    context.watch(r) // Register for termination events
    ActorRefRoutee(r)
  }

  // Notify the worker pool about the initial set of actors
  workerPool ! WorkerPool.ActorsReceive(routees)
```
This code defines an actor called PoolSupervisor that creates a sequence of worker actors and sends them to a worker pool actor. The supervisorStrategy is overridden with the default strategy for actors under this supervisor.

Each worker actor is given a unique name using workerName() and is created using the TextPrinter.props(pauseDuration, sentimentValues) method. The supervisor registers each worker actor for termination events using context.watch(r) and adds them to the routees sequence.

Finally, the PoolSupervisor actor sends the routees sequence to the workerPool actor using the WorkerPool.ActorsReceive(routees) message. The exact functionality of the worker actors and the worker pool is not shown.

**Task 2 (Minimal Task)** --  Create an actor that would mediate the tasks being sent to the Worker Pool. Any tweet that this actor receives will be sent to the Worker Pool in a Round Robin fashion. Direct the Reader actor to sent it’s tweets to this actor.

```scala
        case ActorsReceive(routees) =>
      // receive a message containing a new list of routees
      log.info("Received actors")
      // update the router with the new routees using the RoundRobinRoutingLogic
      router = Router(RoundRobinRoutingLogic(), routees)

    }
```
This is a case in the WorkerPool actor's receive method that handles the ActorsReceive message. The message contains a new list of routees. When the WorkerPool actor receives this message, it logs an information message using log.info("Received actors").

The router variable is then updated with the new routees using the Router constructor with RoundRobinRoutingLogic() as the first argument, and routees as the second argument. This means that the router will use round-robin routing to distribute messages among the new routees.


**Task 3 (Main Task)** -- Continue your Worker actor. Occasionally, the SSE events will contain a “kill message”. Change the actor to crash when such a message is received. Of course, this should trigger the supervisor to restart the crashed actor.


```scala
        case TextPrinter.PrintTweet(tweet) =>
      // Parsing the tweet as JSON
      val text = parse(tweet).getOrElse(Json.Null)
      // Checking if the tweet is null or not
      if (text.isNull) {
        throw new Exception("TWEET IS NULL")
      }
}
```
This code parses a tweet that is in JSON format using the parse method. It then checks whether the parsed tweet is null or not using the isNull method. If the parsed tweet is null, then it throws an exception with the message "TWEET IS NULL".

```scala
      // Remove the dead worker actor from the routees list
      val index = routees.indexOf(deadTextPrinter)
      routees = routees.patch(index, Nil, 1)

      // Create a new worker actor to replace the dead one, and register it for termination events
      val name = workerName()
    
      val newTweetPrinter = context.actorOf(TextPrinter.props(pauseDuration, sentimentValues), name)
      context.watch(newTweetPrinter)
      
    
      workerPool ! WorkerPool.ActorRemove(ActorRefRoutee(deadTextPrinter))
      workerPool ! WorkerPool.ActorAdd(ActorRefRoutee(newTweetPrinter))
```
This code handles the Terminated message, which is sent to an actor when one of its child actors terminates. In this case, the terminated child actor is a worker actor that was processing tweets.

The first thing that the code does is remove the dead worker actor from the list of routees by calling the patch method. It then creates a new worker actor to replace the dead one by calling the actorOf method.

The new worker actor is registered for termination events using the watch method, and a message is sent to the worker pool to remove the dead actor and add the new one using the ActorRemove and ActorAdd messages.

This ensures that the worker pool always has the correct number of worker actors, and that the dead worker actor is replaced by a new one.

## P1W3

**Task 1 (Minimal Task)** -- Continue your Worker actor. Any bad words that a tweet might contain mustn’t be printed. Instead, a set of stars should appear, the number of which corresponds to the bad word’s length. Consult the Internet for a list of bad words.

```scala
          // Replace bad words with stars
      for (word <- badWords) {
        tweetText = tweetText.replaceAll(s"(?i)\\b$word\\b", "*" * word.length)
      }
      // Printing the modified tweet text to console
      println(tweetText)
    
```
This code replaces any occurrences of bad words in the tweet text with a series of asterisks (*) of the same length as the bad word. It then prints the modified tweet text to the console.

The code iterates through each bad word in the badWords list, and for each bad word, it uses the replaceAll method to replace any occurrences of the word with a string of asterisks of the same length as the word. The regular expression (?i)\\b$word\\b is used to match whole words (i.e., not parts of words) that match the bad word, case-insensitively.

## P1W4

**Task 1 (Minimal Task)** --  Continue your Worker actor. Besides printing out the redacted tweet text, the Worker actor must also calculate two values: the Sentiment Score and the Engagement Ratio of the tweet. To compute the Sentiment Score per tweet you should calculate the mean of emotional scores of each word in the tweet text. A map that links words with their scores is
provided as an endpoint in the Docker container. If a word cannot be found in the map, it’s emotional score is equal to 0. The Engagement Ratio should be calculated as follows: engagement ratio = (#favourites + #retweets) / #followers


```scala
          // send an HTTP GET request to the URI
      val responseFuture = Http().singleRequest(HttpRequest(uri = uri))
      responseFuture
        .onComplete {
          case Success(res) =>
            // if the request succeeds, unmarshal the response entity to a string
            Unmarshal(res.entity).to[String]
              .onComplete {
                case Success(json) =>
                  // split the response string into lines
                  json.split('\n').foreach(
                    line => {
                      // split each line into an emotion and a value
                      val emotion = line.split('\t')(0)
                      val value = line.split('\t')(1)
                      // add the emotion and value to the map of sentiments
                      mapSentiments += (emotion -> value.toDouble)
                    }
                  )

                  // send the map of sentiments back to the sender actor
                  sendTo ! mapSentiments
                case Failure(_) => sys.error("something wrong")
              }
    }
```

This code is performing an HTTP GET request to a specified URI and expects a response with emotions in the form of a string. If the request is successful, the response string is split into lines and each line is split into an emotion and a value. Then, the emotion and value are added to a mutable map called mapSentiments. Finally, the map of sentiments is sent back to the actor that initiated the request using the sendTo reference.

The onComplete method is used to handle both successful and failed responses. If the response is successful, the json string is split into lines using the newline character (\n) as a delimiter. Then, each line is split into two parts using the tab character (\t) as a delimiter. The first part represents the emotion, while the second part represents its corresponding value. These emotions and values are added to the mutable map mapSentiments.

If the response is not successful, an error message is printed to the console. It is important to note that the code does not handle any exceptions that may occur during the HTTP request or response handling.

```scala

  
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

```

This function takes in a string as input and computes a sentiment score for that string. The sentiment score is calculated by first splitting the input string into individual words, and then mapping each word to its corresponding sentiment value (which is looked up from a map called sentimentValues). If a word is not found in sentimentValues, its sentiment value is set to 0.0. The mean of all the emotion scores is then calculated and returned as the sentiment score for the input string.

```scala

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
```

This is a private method that takes a JSON object representing a tweet as input and computes the engagement ratio of the tweet as a string.

First, it extracts the user object from the tweet JSON using a helper method called getJsonProperty. Then, it retrieves the favourites_count, followers_count, and retweet_count properties from both the tweet and user objects.

If the user has no followers, the method returns "0" as the engagement ratio to avoid a divide-by-zero error. Otherwise, it calculates the engagement ratio as the sum of favorites and retweets divided by followers, and converts the result to a string.

## P1W5

**Task 1 (Minimal Task)** -- Create an actor that would collect the redacted tweets from Workers and would print them in batches. Instead of printing the tweets, the Worker should now send them to the Batcher, which then prints them. The batch size should be parametrizable.

```scala
class Batcher(batchSize: Int) extends Actor with ActorLogging {
   changes after initialization
  private val tweetBatch: mutable.Queue[String] = mutable.Queue[String]()
  private var currentBatchSize: Int = 0
  override def receive: Receive = {
    case GetTweet(tweet) =>
      if (currentBatchSize < batchSize) {
        currentBatchSize += 1
        tweetBatch.enqueue(tweet)
      } else {
        currentBatchSize = 0
        while (tweetBatch.nonEmpty) {
          val tweet = tweetBatch.dequeue
          log.info(tweet)
        }
      }
  }
}
```
This code defines an actor that batches tweets. It receives GetTweet(tweet) messages and adds the tweets to a mutable queue tweetBatch. When the queue size reaches the batchSize, it logs all the tweets and clears the queue.

The actor has a mutable state, which can lead to synchronization issues in a concurrent environment. Also, the currentBatchSize variable is not necessary since it can be computed from the tweetBatch queue size. The while loop used to log the tweets and clear the queue is blocking, and it can prevent the actor from processing other messages while it executes.

**Task 2 (Main Task)** --Continue your Batcher actor. If, in a given time window, the Batcher does not receive enough data to print a batch, it should still print it. Of course, the actor should retain any existing behaviour. The time window should be parametrizable.

```scala
timers.startTimerAtFixedRate(TickCancellationKey, PrintTweets, timerTime)

else {
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
  
```

This code appears to be part of an actor that batches tweets and prints them periodically.

The startTimerAtFixedRate method starts a timer that sends PrintTweets messages to the actor every timerTime interval. If the current batch is full, the actor prints the tweets, resets the batch, and starts the timer again. The timers.cancel(TickCancellationKey) line cancels any previously scheduled timer with the same key before starting a new one.

The PrintTweets message handler prints the size of the current batch and dequeues the tweets from the tweetBatch queue and logs them. The currentBatchSize variable is set to 0, indicating that a new batch will start receiving tweets.

This implementation has the same issues as the previous code: mutable state and blocking while loop. Also, the println statement may cause concurrency issues when multiple threads try to print to the console at the same time.

## P1W6

**Task 1 (Minimal Task)** --Create a database that would store the tweets processed by your system.

```scala
override def receive: Receive = {
  case GetTweet(id) =>
    val tweet = jedis.hget("tweets", id)
    sender() ! mapper.readValue(tweet, classOf[String])
  case CreateTweet(tweet, id) =>
    jedis.hset("tweets", id, tweet)
  case GetTweets(count) =>
    val tweets = jedis.hgetAll("tweets")
    val returnTweets = tweets.values().toArray().map(tweet => mapper.readValue(tweet.toString, classOf[String])).take(count.getOrElse(tweets.size()))
    sender() ! returnTweets
}

```
The code above is an implementation of an Akka actor called TweetDb, which interacts with a Redis database to store and retrieve tweets. The actor defines a receive method that handles messages sent to it. The actor can receive three types of messages: GetTweet, CreateTweet, and GetTweets.

When the actor receives a GetTweet message with an ID, it retrieves the corresponding tweet from Redis and sends it back to the sender. When it receives a CreateTweet message with a tweet and an ID, it stores the tweet in Redis with the given ID. When it receives a GetTweets message with a count (optional), it retrieves all tweets from Redis and sends them back to the sender. The number of tweets returned is either the specified count or all tweets if the count is not specified.

The actor uses the Jackson JSON library to serialize and deserialize tweet data, and the Jedis client to communicate with Redis.

**Task 2 (Minimal Task)** --Continue your Batcher actor. Instead of printing the batches of tweets, the
actor should now send them to the database, which will persist them.

```scala
case SaveTweets =>
println(s"Saving ${currentBatchSize} batches!")
currentBatchSize = 0
while (tweetBatch.nonEmpty) {
val tweet = tweetBatch.dequeue
val guid = java.util.UUID.randomUUID.toString
tweetsDatabase ! TweetDb.CreateTweet(tweet, guid)
}
self ! PrintSavedTweets

case PrintSavedTweets =>
val random = scala.util.Random
val count = random.nextInt(2)
if (count == 1) {
val future = tweetsDatabase ? TweetDb.GetTweets(Some(10))
val tweets = Await.result(future, timeout.duration).asInstanceOf[Array[String]]
tweets.foreach(tweet => log.info(tweet))
}
```

The first part of the code block is triggered when the actor receives a SaveTweets message. It dequeues tweets from a temporary queue, generates a GUID for each tweet, and sends a CreateTweet message with the tweet and GUID as parameters to the tweetsDatabase actor. Once all the tweets have been saved, it sends a PrintSavedTweets message to itself.

The second part of the code block is triggered when the actor receives a PrintSavedTweets message. It generates a random number, and if it is 1, it sends a GetTweets message with a limit of 10 to the tweetsDatabase actor. It then waits for the future to complete, gets the result as an array of strings, and prints each tweet using the actor's log method.

## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).
- Akka [Actors](https://developer.lightbend.com/guides/akka-quickstart-scala/index.html) Quickstart with Scala.
- Introduction to [Actors](https://doc.akka.io/docs/akka/current/typed/actors.html).
- Documentation of [Redis](https://redis.io/docs/).
