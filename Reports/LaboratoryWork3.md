# FAF.PTR16.1 -- Project 1

> **Performed by:** Mustuc Mihai, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P1W2

**General Requirements** -- The goal for this project is to create an actor-based message broker application that would
manage the communication between other applications named producers and consumers.
Your project should be tracked in a VCS repository. Every feature you work on should be
easily verifiable. Your system should provide logs about starting / stopping actors, connecting
/ subscribing consumers and other events happening.

**Minimal Features** --

***The message broker represents a dedicated TCP / UDP server**;*

```scala
   class TcpServer(host: String, port: Int, broker: ActorRef) extends Actor {
  import context.system

  // Bind to the specified host and port
  IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))

  def receive = {
    case Bound(localAddress) =>
      println(s"TCP server bound to $localAddress")
    case CommandFailed(_: Bind) =>
      println(s"TCP server bind failed")
      context.stop(self)
    case Connected(remote, local) =>
      // Create a new ClientHandler actor for each client connection
      val handler = context.actorOf(Props(new ClientHandler(remote, broker)))
      val connection = sender()
      connection ! Register(handler)
  }
}

```
The TcpServer class is an actor that binds to a specified host and port, and when a client connects, it creates a new ClientHandler actor to handle the client's communication with the broker. It uses the IO(Tcp) actor to interact with the underlying TCP functionality.

The TcpServer actor receives three types of messages:

Bound(localAddress): This message is received when the server successfully binds to the specified address. It indicates that the server is bound and ready to accept client connections.

CommandFailed(_: Bind): This message is received if the server fails to bind to the specified address. It indicates that the server binding failed, and the server actor stops itself.

Connected(remote, local): This message is received when a client successfully connects to the server. It creates a new ClientHandler actor to handle the client's communication and registers it with the client's connection actor.

![BrokerConsole](https://cdn.discordapp.com/attachments/1108069055702773760/1108069078867923185/image.png)

***The message broker allows for clients connerction*** 
```scala
  class ClientHandler(remote: InetSocketAddress, broker: ActorRef) extends Actor {
  import Tcp._
  import MessageBroker._

  println(s"Client connected from $remote")

  // Regular expressions for matching subscribe and publish patterns
  private val publishPattern = """PUBLISH\(Map\((.+)\)\)""".r
  val subscribe: Regex = """ArrayBuffer\(([\w\s,]+)\)""".r

  def receive = {
    case Received(data) =>
      val input = data.utf8String
      println(s"Received data: $input")

      input match {
        // For subscribe commands, it extracts the topics and
        // subscribes the client to each topic.
        case subscribe(matchResult) =>
          ...

          //For publish commands, it extracts the topic-message pairs and
        // sends them to the broker for publishing.
        case publishPattern(topicMessagePairs) =>
          ...

        case _ =>
          println("Invalid input format")
      }

    case PeerClosed =>
      println(s"Client from $remote disconnected")
      context.stop(self)
  }
```

The ClientHandler actor has the following responsibilities:

1)Print a message when a client is connected.
2)Parse the received data and perform the appropriate action based on the input.
3)Handle the case when the client is disconnected.

***The message broker provides the ability to subscribe to multiple topics (if you are a consumer) and publish messages on different topics (if you are a publisher);*** 

```scala
        case subscribe(matchResult) =>
          // Extract topics from the match result
          val topics = splitTopicString(matchResult)
          val topicsBuffer = ArrayBuffer[String]()
          for (topic <- topics) {
            topicsBuffer += topic
          }
          val subscriber = sender()
          println(s"Subscribing to $topicsBuffer")
          // Assign the subscriber to each topic
          for (topic <- topicsBuffer) {
            broker ! MessageBroker.Subscribe(topic, subscriber)
          }

          //For publish commands, it extracts the topic-message pairs and
        // sends them to the broker for publishing.
        case publishPattern(topicMessagePairs) =>
          // Extract topic and message pairs from the input
          val topicMessageRegex = """(\w+)\s*->\s*([^,]+)""".r
          val topicMessageMatches = topicMessageRegex.findAllMatchIn(topicMessagePairs)
          topicMessageMatches.foreach { matchResult =>
            val topic = matchResult.group(1)
            val message = matchResult.group(2)
            println(s"Topic: $topic, Message: $message")
            broker ! Publish(topic, message)
          }
```


The ClientHandler actor in the message broker system handles incoming messages from clients. It expects two types of messages: subscribe commands and publish commands.

For subscribe commands, the actor extracts the topics from the received input and assigns them to a temporary buffer. It then retrieves the reference to the subscriber actor, which represents the client that sent the message. The actor iterates over the topics in the buffer and sends a MessageBroker.Subscribe message to the broker actor for each topic, indicating that the subscriber wants to subscribe to that topic.

For publish commands, the actor extracts the topic-message pairs from the received input. It uses regular expressions to match and extract the topic and message for each pair. For each pair, the actor sends a Publish message to the broker actor, indicating that the message should be published on the specified topic.

These cases handle the logic of subscribing clients to topics and publishing messages to the broker based on the received input.

![ProducerConsole](https://cdn.discordapp.com/attachments/1108069055702773760/1108069307029659699/image.png) 

![ConsumerConsole](https://cdn.discordapp.com/attachments/1108069055702773760/1108069706117697556/image.png) 

***The project has an executable that can run the message broker with a single click /command.*** 

To create an executable for your message broker project that can be run with a single click or command, I choose to duild an executable JAR file: 

 ![JARFile](https://cdn.discordapp.com/attachments/1108069055702773760/1108120136264716288/image.png) 

 ![JARFileConsle](https://cdn.discordapp.com/attachments/1108069055702773760/1108076336490233867/image.png) 


## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).
- Akka [Actors](https://developer.lightbend.com/guides/akka-quickstart-scala/index.html) Quickstart with Scala.
- Introduction to [Actors](https://doc.akka.io/docs/akka/current/typed/actors.html).
- Publish & Subscribe [MQTT Essentials](https://www.hivemq.com/blog/mqtt-essentials-part2-publish-subscribe/).
- [Introduction to MQTT](https://www.hivemq.com/blog/mqtt-essentials-part2-publish-subscribe/) QoS (Quality of Service) MQTT Essentials.
