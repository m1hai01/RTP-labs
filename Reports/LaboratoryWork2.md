# FAF.PTR16.1 -- Project 0

> **Performed by:** Mustuc Mihai, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P0W3 – An Actor is Born

**Task 1 (Minimal Task)** -- Create an actor that prints on the screen any message it receives.

```scala
  class PrinterActor extends Actor {
  def receive = {
    case message: Any =>
      println(message)
  }
}
```
In my implementation, receive method is a required method for actors, and it defines how an actor should handle incoming messages. In this case, the receive method matches on any type of message (Any) and simply prints it to the console using the println method.

**Task 2 (Minimal Task)** -- Create an actor that returns any message it receives, while modifying it. 

```scala
  class ModifierActor extends Actor {
  def receive = {
    case i: Int =>
      val modified = i + 1
      println(s"Received: $i, Modified: $modified")
      sender() ! modified

    case s: String =>
      val modified = s.toLowerCase
      println(s"Received: $s, Modified: $modified")
      sender() ! modified

    case _ =>
      val modified = "I don't know how to HANDLE this!"
      println(s"Received unknown message, Modified: $modified")
      sender() ! modified
  }

}
```
In my implementation, receive method is the entry point for processing messages in an actor, and it pattern matches on the type of the incoming message using case statements.

The first case statement matches on an incoming Int and adds 1 to it, printing out the original and modified values. It then sends the modified value back to the sender using the sender() method.

The second case statement matches on an incoming String and converts it to lowercase, printing out the original and modified values. It then sends the modified value back to the sender using the sender() method.

The third case statement matches on any other type of message using the _ wildcard pattern. It prints out a message saying it doesn't know how to handle the message, and sends back a default modified message "I don't know how to HANDLE this!" to the sender.

**Task 3 (Minimal Task)** -- Create a two actors, actor one ”monitoring” the other. If the second actor
stops, actor one gets notified via a message.

```scala
    class MonitoringActor(monitored: ActorRef) extends  Actor{
  //register itself to receive a Terminated message from
  // the actor system when the monitored actor stops.
  context.watch(monitored)

  def receive = {
    case Terminated(actor) if actor == monitored =>
      println("Monitored actor stopped!")
  }
  }


  class MonitoredActor extends Actor{
  def receive = {
    case msg: String => println(s"Received message: $msg")
  }
```

In my implementation, MonitoringActor is defined to take an ActorRef parameter named monitored, which is the reference to the actor that it will monitor. In the receive method, the actor registers itself to receive a Terminated message from the actor system when the monitored actor stops.

If the Terminated message is received and the actor parameter matches the monitored actor, then the MonitoringActor prints a message saying that the monitored actor has stopped.

But, the MonitoredActor is defined to receive messages of type String and prints out the received message.

The context.watch() method is used to register the MonitoringActor to receive Terminated messages when the monitored actor stops. This allows the MonitoringActor to be notified when the monitored actor has terminated, and take appropriate actions based on this information.

**Task 4 (Minimal Task)** --  Create an actor which receives numbers and with each request prints out the
current average.

```scala
    class Averager extends Actor{
  //val log = Logging(context.system, this)
  var sum: Double = 0
  var avarage: Double = 0

  def receive = {
    case x: Int =>
      sum = avarage + x
      avarage = sum / 2
      println(s"Current average is $avarage")
      //log.info(s"Current average is $avarage")
      sender() ! s"Current average is $avarage"
  }
}
```
In my implementation, Averager actor maintains two variables, sum and average, that keep track of the sum of the received numbers and the current average, respectively.

When the actor receives an Int message, it updates the sum variable by adding the received number to the previous sum. It then computes the new average by dividing the sum by 2 (since the actor only keeps track of the last two numbers received).

The actor prints out the current average to the console using println and sends back a message to the sender containing the current average using the sender() method.



**Task 1 (Main Task)** --  Create an actor which receives numbers and with each request prints out the
current average.

```scala
    class QueueActor extends Actor{
  var queue = ArrayBuffer[Int]()

  override def receive: Receive = {
    case x: Int =>
      queue = queue :+ x
      println("ok ")
    case "pop" => {
      if (queue.isEmpty) {
        println("Queue is empty")
      }
      else {
        println(queue(0))
        queue.remove(0)
      }
    }
  }

}
```
In my implementation, QueueActor maintains a mutable ArrayBuffer[Int] named queue that stores the integers in the queue. When the actor receives an Int message, it adds the integer to the end of the queue using the :+ operator.

When the actor receives a "pop" message, it first checks if the queue is empty using the isEmpty method of the queue variable. If the queue is empty, the actor prints the message "Queue is empty". Otherwise, the actor retrieves the first element of the queue using the queue(0) syntax, prints the element to the console, and removes it from the queue using the remove method.

## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).