# FAF.PTR16.1 -- Project 0

> **Performed by:** Mustuc Mihai, group FAF-201<br>
> **Verified by:** asist. univ. Alexandru Osadcenco

## P0W1

**Task 1 (Minimal Task)** -- Write a script that would print the message “Hello PTR” on the screen.
Execute it.

```scala
  def main(args: Array[String]): Unit = {
    println("Hello PTR");
```

Here I have created an object Main which contains a static method that displays in the console "Hello PTR".
 
 **Bonus** -- Create a unit test for your project.
 ```scala
 class CylinderAreaTest extends AnyFunSuite {

  test("CylinderArea.area") {
    val m = new MinimalTasksClass
    assert(m.cylinderArea(2, 3) == 94.24777960769379)
  }
}
```
It is a unit test for Cylinder Area Task.

**Bonus** -- Create a unit test for your project.
 ```scala
 class HelloPtrTest extends  AnyFunSuite {
  test("HelloPtr.hello") {
    val m = new MinimalTasksClass
    assert(m.helloPTR() == "Hello PTR")
  }
}
```
It is a unit test for Hello World function.

## P0W2

**Task 1 (Minimal Task)** -- Write a function that determines whether an input integer is prime.

```scala
  def isPrime(n: Int): Boolean = {
      // check if n is divisible by any number in the range 2 until n.
      if (n <= 1) return false
      for (i <- 2 until n) {
        if (n % i == 0) return false
      }
      true
  }
```


**Task 2 (Minimal Task)** -- Write a function to calculate the area of a cylinder, given it’s height and radius.

```scala
    def cylinderArea(h: Double , r: Double): Double = {
      val pi :Double = 3.141592653589793238
      val surface: Double = 2 * pi * r * h + 2 * pi * r * r
      surface
    }
```



**Task 3 (Minimal Task)** -- Write a function to reverse a list.

```scala
    def reverse(s: List[Int]): List[Int] = {
      val result  = s.reverse
      result
    }
}
```



**Task 4 (Minimal Task)** -- Write a function to calculate the sum of unique elements in a list.

```scala
    def uniqueSum(s: List[Int]): Int = {
      val value = s.distinct
      val sum = value.sum
      sum
    }
```


**Task 5 (Minimal Task)** -- Write a function that extracts a given number of randomly selected elements from a list.

```scala
    def extractRandomN(s: ListBuffer[Int], n: Int): ListBuffer[Int] ={

      var arr = ListBuffer[Int]()
      for (i <- 0 until n) {
        val randomIndex = Random.nextInt(s.length)
        val randomElement = s(randomIndex)
        s -= randomElement
        arr += randomElement
      }
      arr
    }
```

I find the random index and then based on that I find the random number.

**Task 6 (Minimal Task)** -- Write a function that returns the first n elements of the Fibonacci sequence.

```scala
    def firstFibonacciElements(n: Int): List[Int] = {
      if (n <= 0) return List()
      if (n == 1) return List(0)
      if (n == 2) return List(0, 1)

      var result = List(0, 1)
      for (i <- 2 until n) {
        val next = result(i - 1) + result(i - 2)
        result = result :+ next
      }

      result
    }
```

Formula: F(n) = F(n-1) + F(n-2), where F(0) = 0 and F(1) = 1.

**Task 7 (Minimal Task)** -- Write a function that, given a dictionary, would translate a sentence. Words not found in
the dictionary need not be translated.

```scala
    def translator(dictionary: Map[String, String], originalString: String): String = {
      originalString.split(" ").map(word => dictionary.getOrElse(word, word)).mkString(" ")
```

The function splits the original string into words using the split method, then maps each word to its corresponding translation in the dictionary, using the getOrElse method. If the word is not found in the dictionary, it is returned as-is. The result of the map operation is an array of translated words. Finally, the mkString method is used to join the array of words back into a single string, separated by spaces.

**Task 8 (Minimal Task)** --Write a function that receives as input three digits and arranges them in an order that
would create the smallest possible number. Numbers cannot start with a 0.

```scala
  def smallestNumber(a: Int, b: Int, c: Int): Int = {
    val numbers = List(a, b, c)
    val sortedNumbers = numbers.sorted
    if (sortedNumbers.head == 0) {
      sortedNumbers(1) * 100 + sortedNumbers.head * 10 + sortedNumbers(2)
    } else {
      sortedNumbers.head * 100 + sortedNumbers(1) * 10 + sortedNumbers(2)
    }
  }
```

I first create a list of the three input digits. Then, I sort the list using the sorted method. Finally, I check if the first (i.e., the smallest) element is 0. If it is, then I return the number obtained by concatenating the second and third elements in the given order, with the first element in the middle. If the first element is not 0, then I return the number obtained by concatenating the elements in the order they appear in the sorted list.

**Task 9 (Minimal Task)** -- Write a function that would rotate a list n places to the left.

```scala
  def rotateLeft(s: List[Int], n: Int): List[Int] = {
    val newStartIndex = n % s.length
    s.drop(newStartIndex) ++ s.take(newStartIndex)
  }
```

First, I find the index from which the new list should start by using the modulo operator on n and the length of the list. This ensures that if n is greater than the length of the list, I still get the correct starting index.

Next, I use the drop method to remove the first newStartIndex elements of the list and the take method to get the first newStartIndex elements of the list. Finally, I concatenate these two lists using the ++ operator to get the rotated list.

**Task 10 (Minimal Task)** -- Write a function that lists all tuples a, b, c such that `a^2 + b^2 = c^2`
and `a, b ≤ 20`.

```scala
  def listRightAngleTriangles(): List[(Int, Int, Int)] = {
    var triangles = List[(Int, Int, Int)]()
    for (a <- 1 to 20) {
      for (b <- a to 20) {
        val c = Math.sqrt(a * a + b * b).toInt
        if (c * c == a * a + b * b && c <= 20) {
          triangles = (a, b, c) :: triangles
        }
      }
    }
    triangles
  }
```

This function loops through all the possible combinations of a and b between 1 and 20, calculates the corresponding value of c using the Pythagorean theorem, and adds the tuple (a, b, c) to the list triangles if c is a whole number and is less than or equal to 20. The final list of right-angled triangles is returned by the function.

**Task 11 (Main Task)** -- Write a function that eliminates consecutive duplicates in a list.

```scala
  def removeConsecutiveDuplicates(s: List[Int]): List[Int] = {
    var result = List[Int]()
    var lastElement = Int.MinValue
    for (element <- s) {
      if (element != lastElement) {
        result = result :+ element
        lastElement = element
      }
    }
    result
  }
}
```

In this implementation, I use a result list to store the elements of the list after removing the consecutive duplicates. I also use a variable lastElement to keep track of the last element I added to the result list. In the for loop, I iterate over each element in the input list. If the current element is different from the last element, I add it to the result list and update the lastElement variable.

**Task 12 (Main Task)** -- Write a function that, given an array of strings, will return the words that can
be typed using only one row of the letters on an English keyboard layout.

```scala
    def lineWords(words: Array[String]): Array[String] = {
    val rows = List("qwertyuiop", "asdfghjkl", "zxcvbnm")
    words.filter(word => rows.exists(row => word.toLowerCase.forall(c => row.contains(c))))
  }
```
I implement the  function that takes an array of strings words as input and defines a list rows that represents the rows of the English keyboard. It then uses the filter method on the words array to return a new array that contains only the words that can be typed using one row of the keyboard. The condition for filtering checks if the word can be typed using any of the rows, which is done by checking if all characters in the word are contained in the row using the forall method.

**Task 13 (Main Task)** -- Write a function that eliminates consecutive duplicates in a list.

```scala
    def encode(s: String, n: Int): String = {
    s.map(c => (c + n).toChar)
  }
  def decode(s: String, n: Int): String = {
    s.map(c => (c - n).toChar)
  }
```
In my implementation I using the map method to apply a transformation to each character c in the string s. The transformation is taking the ASCII value of c and adding the shift value n to it, then converting the resulting value back to a character using the toChar method. This effectively shifts the character by n positions in the ASCII table.

**Task 14 (Main Task)** -- White a function that, given a string of digits from 2 to 9, would return all
possible letter combinations that the number could represent (think phones with buttons).


```scala
    def letterCombinations(s: String): List[String] = {
    if (s.isEmpty) return List()

    val phoneMap = Map(
      '2' -> "abc",
      '3' -> "def",
      '4' -> "ghi",
      '5' -> "jkl",
      '6' -> "mno",
      '7' -> "pqrs",
      '8' -> "tuv",
      '9' -> "wxyz"
    )

    s.foldLeft(List("")) { (combinations, s) =>
      for {
        combination <- combinations
        char <- phoneMap(s)
      } yield combination + char
    }
  }
}
```
In my implementation I use phoneMap map to look up the set of letters for each digit, but instead of using recursion, it uses the foldLeft method to iterate over the input string and build up the list of combinations.

The foldLeft method takes two arguments: an initial value and a function. The initial value is an empty list containing a single empty string, since this is the only possible combination for an empty input string. The function takes two arguments: the current list of combinations and the current digit being processed. It then generates a new list of combinations by iterating over the current list of combinations and the set of letters for the current digit, and concatenating each combination with each letter.

The resulting list of combinations is then returned by the function.



**Task 15 (Main Task)** -- White a function that, given an array of strings, would group the anagrams
together.

```scala
    def groupAnagrams(s: Array[String]): Map[String, List[String]] = {
    val groups = s.groupBy(_.sorted)
    groups.view.mapValues(_.toList).toMap
  }
```
In my implementation first groups the input array strs by sorting each string in the array and using the sorted version as the grouping key. The resulting groups variable is a Map where each key is a sorted string, and each value is a list of strings from the input array that are anagrams of each other.

The function then uses mapValues to transform the values in the groups map from arrays to lists, which is the expected output format.

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

## P0W4 – The Actor is dead.. Long live the Actor

**Task 1 (Minimal Task)** --  Create an actor which receives Create a supervised pool of identical worker actors. The number of actors is static, given at initialization. Workers should be individually addressable. Worker actors should echo any message they receive. If an actor dies (by receiving a “kill” message), it should be restarted by the supervisor. Logging is welcome.

```scala
   class SupervisorClass extends Actor with ActorLogging {

  import SupervisorActor._
//The defaultStrategy is used, which means that
// the supervisor will restart the worker actor
// if it throws an exception.
  override val supervisorStrategy: SupervisorStrategy =
    SupervisorStrategy.defaultStrategy

  //distribute messages among a group of actors.
  //what logic of transmiting messages
  private val router: Router = {
    Router(RoundRobinRoutingLogic(), routees)
  }
  private val workerCount = 3
  //the group of worker actors
  // that the supervisor is responsible for supervising
  private var routees = IndexedSeq.fill(workerCount) {
    val name = workerName()
    // Each worker actor is created
    val r = context.actorOf(PrintActorObject.props(name), name)
    context.watch(r)
    ActorRefRoutee(r)
  }

  println(routees)

  override def receive: Receive = {
    case SendMessage(message) =>
      log.info("Sending messages to all workers: {}", message)
      router.route(PrintActorObject.Print(message), sender())
      //transmit the list of references of workes back to the sender
    case SendWorkers =>
      log.info("Sending workers")
      sender() ! routees
    case Terminated(ref) =>
      log.info("Worker {} terminated, restarting...", ref.path.name)

      val index = routees.indexOf(ref.actorRef)
      routees = routees.patch(index, Nil, 1)

      val name = workerName()
      val r = context.actorOf(PrintActorObject.props(name), name)
      context.watch(r)
      router.removeRoutee(ref)
      router.addRoutee(ActorRefRoutee(r))
  }

  private def workerName(): String = {
    s"worker-${java.util.UUID.randomUUID().toString}"
  }
}
```

The SupervisorClass defines an actor that is responsible for creating and supervising a group of PrintActorClass workers. It creates a router that distributes messages among the worker actors using round-robin routing logic. It also handles the termination of worker actors by removing the terminated actor from the router and creating a new worker actor to replace it.

The SendMessage message can be sent to the SupervisorActor to distribute a message to all the worker actors. The SendWorkers message can be sent to the SupervisorActor to receive a list of references to the worker actors.

```scala
class PrintActorClass(name: String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Print(message) =>
      log.info("Worker Actor {} received message: {}", name, message)
      sender() ! s"Worker Actor $name print: $message"
    case Kill =>
      log.warning("Worker Actor {} received Kill ", name)
      throw new Exception(s"Worker Actor $name is killed")
  }

```

The PrintActorClass defines an actor that can receive two types of messages: Print and Kill. When it receives a Print message, it logs the message and sends back a response to the sender with the message content. When it receives a Kill message, it logs a warning and throws an exception to simulate a failure.

**Task 2 (Main Task)** --  Create a supervised processing line to clean messy strings. The first worker in the line would split the string by any white spaces (similar to Python’s str.split method).The second actor will lowercase all words and swap all m’s and n’s (you nomster!). The third
actor will join back the sentence with one space between words (similar to Python’s str.join method). Each worker will receive as input the previous actor’s output, the last actor printing the result on screen. If any of the workers die because it encounters an error, the whole processing line needs to be restarted. Logging is welcome.

```scala
  class Supervisor1 extends Actor with ActorLogging {
  override val supervisorStrategy: SupervisorStrategy =
    AllForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1.minute,
      loggingEnabled = true
    )(SupervisorStrategy.defaultDecider)

  private val router: Router = {
    Router(RoundRobinRoutingLogic(), routees)
  }
  private var routees = createRoutees()
  private var splitterRef: ActorRef = _

  override def receive: Receive = {
    case SupervisorObject.SendMessage(message) =>
      splitterRef ! StringSplitter.SplitMessage(message)
    case Terminated(ref) =>
      log.info("Worker {} terminated, restarting...", ref.path.name)

      val newRoutees = createRoutees()
      for (r <- routees) {
        router.removeRoutee(r)
      }

      for (r <- newRoutees) {
        router.addRoutee(r)
      }
  }

  private def createRoutees(): IndexedSeq[ActorRefRoutee] = {
    var result = IndexedSeq.empty[ActorRefRoutee]

    val printer = context.actorOf(StringPrinter.props(), workerName())
    val joiner = context.actorOf(JoinerObj.props(printer), workerName())
    val lowercaserAndSwapper = context.actorOf(StringLowercaserAndSwapper.props(joiner), workerName())
    val splitter = context.actorOf(StringSplitter.props(lowercaserAndSwapper), workerName())
    splitterRef = splitter

    context.watch(printer)
    context.watch(splitter)
    context.watch(lowercaserAndSwapper)
    context.watch(joiner)

    result = result :+ ActorRefRoutee(printer)
    result = result :+ ActorRefRoutee(splitter)
    result = result :+ ActorRefRoutee(lowercaserAndSwapper)
    result = result :+ ActorRefRoutee(joiner)

    result
  }
```

The main actor in the system is the Supervisor1 actor, which has the responsibility of creating and supervising the other actors. The Supervisor1 actor receives messages of type SendMessage, which contain a string message that needs to be processed. The string message is first passed to the SplitterActor actor, which splits the message into an array of words and passes it to the LowerAndSwapActor actor. The LowerAndSwapActor actor converts all the characters to lowercase and swaps all "m" and "n" characters before passing the processed message to the JoinerActor actor. The JoinerActor actor concatenates the words into a single string and passes it to the PrinterActor actor, which logs the final message to the console.

```scala
class SplitterActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringSplitter.SplitMessage(message) =>
      if (message.contains('@')) {
        throw new IllegalArgumentException("Message is empty")
      }
      val words = message.split("\\s+")
      nextActor ! LowercaseAndSwap(words)
  }

  class LowerAndSwapActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringLowercaserAndSwapper.LowercaseAndSwap(words) => {
      val lowercasedWords = words.map(_.toLowerCase)
      val swappedMsWithNs = lowercasedWords.map(str => str.map {
        case 'm' => 'n'
        case 'n' => 'm'
        case c => c
      })

      nextActor ! JoinMessage(swappedMsWithNs)
    }
  }

  class JoinerActor(nextActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case JoinerObj.JoinMessage(message) =>
      val joinedWords = message.mkString(" ")
      nextActor ! StringPrinter.PrintMessage(joinedWords)
  }

  class PrinterActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case StringPrinter.PrintMessage(message) =>
      log.info(message)
  }

```

The SplitterActor actor is responsible for splitting a string into words and sending them to the next actor in the pipeline. It receives a message of type StringSplitter.SplitMessage and checks if the string contains the "@" character.

The LowerAndSwapActor actor is responsible for converting each word in a message to lowercase and swapping the "m" and "n" characters. It receives a message of type StringLowercaserAndSwapper.LowercaseAndSwap containing an array of words, converts each word to lowercase using the toLowerCase() method, and swaps the "m" and "n" characters using a map operation.

The JoinerActor actor is responsible for joining the words in a message into a single string and sending it to the next actor.

The PrinterActor actor is responsible for printing the final string to the console.

## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).
- Akka [Actors](https://developer.lightbend.com/guides/akka-quickstart-scala/index.html) Quickstart with Scala.
-  Introduction to [Actors](https://doc.akka.io/docs/akka/current/typed/actors.html).
