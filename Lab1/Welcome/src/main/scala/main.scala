import ActorLab.Main1.QueueActor
import ActorLab.Minimal1.PrinterActor
import ActorLab.Minimal2.ModifierActor
import ActorLab.Minimal3.{MonitoredActor, MonitoringActor}
import ActorLab.Minimal4.Averager
import Lab1.MinimalTasksClass
import akka.actor.{Actor, PoisonPill}
import akka.pattern.ask
//import akka.actor.typed.ActorSystem
import akka.actor._

import scala.collection.mutable.ListBuffer
import akka.actor.Props

import scala.collection.mutable.ListBuffer
import akka.actor.Actor
import akka.actor.ActorSystem
object main {
  def main(args: Array[String]): Unit = {

    val minimalTasksClass = new MinimalTasksClass

    /*//Hello PTR
    val hello = minimalTasksClass.helloPTR()
    println(s"$hello \n")

    //TASK 1
    val num = 17
    val result = minimalTasksClass.isPrime(num)
    println(s"TASK 1: $result $num is a prime number. \n")

    //TASK 2
    val result1 = minimalTasksClass.cylinderArea(3, 4)
    println(s"TASK 2: $result1 is cylinder area \n")


    //TASK 3
    val list = List(1, 2, 4, 8, 4)
    val result2 = minimalTasksClass.reverse(list)
    println(s"TASK 3: $result2 is reverse of list \n")


    //TASK 4
    val list1 = List(1, 2, 4, 8, 4)
    val result3 = minimalTasksClass.uniqueSum(list1)
    println(s"TASK 4: $result3 is sum  of unique elements in list\n")

    //TASK 5
    val list2 = ListBuffer(1, 2, 4, 8, 4)
    val result4 = minimalTasksClass.extractRandomN(list2, 3)
    println(s"TASK 5: $result4 randomly selected elements  from a list\n")

    //TASK 6
    val result5 = minimalTasksClass.firstFibonacciElements(7)
    println(s"TASK 6: $result5 is the first 7 Fibonacci ELements\n")

    //TASK 7
    println(s"TASK 7")
    val dictionary = Map("mama" -> "mother", "papa" -> "father")
    val originalString = "mama is with papa"
    val translatedString = minimalTasksClass.translator(dictionary, originalString)
    println(s"Original string: $originalString")
    println(s"Translated string: $translatedString \n")

    //TASK 8

    println(s"TASK 8")
    val small1 = minimalTasksClass.smallestNumber(4, 5, 3)
    val small2 = minimalTasksClass.smallestNumber(0, 3, 4)
    println(s"First ex: $small1")
    println(s"Second ex: $small2 \n")


    //TASK 9
    val lst = List(1, 2, 4, 8, 4)
    val rotatedList = minimalTasksClass.rotateLeft(lst, 3)
    println(s"TASK 9: $rotatedList is rotate left list \n")

    //TASK 10
    val rs = minimalTasksClass.listRightAngleTriangles()
    println(s"TASK 10: All tuples a, b, c such that a^2+b^2 = c^2 and a, b ≤ 20: \n  $rs \n")

    //TASK 11
    val lst1 = List(1, 2, 2, 2, 4, 8, 4)
    val res1 = minimalTasksClass.removeConsecutiveDuplicates(lst1)
    println(s"TASK 11: Eliminates consecutive duplicates in a $lst1 : $res1 \n")


    //TASK 12
    val words = Array("Hello", "Alaska", "Dad", "Peace")
    val oneLineWords = minimalTasksClass.lineWords(words)
    val concat = oneLineWords.mkString(", ")
    println(s"TASK 12: Words that can be typed using only one row of the letters on an English keyboard layout:")
    println(concat)


    //TASK 13
    val encode = minimalTasksClass.encode("lorem", 3)
    val decode = minimalTasksClass.decode("oruhp", 3)
    println(s"TASK 13: Encode: $encode")
    println(s"TASK 13: Decode: $decode \n")

    //TASK 14
    val task14 = minimalTasksClass.letterCombinations("23")
    println(s"TASK 14: All possible letter combinations $task14 \n")

    //TASK 15

      val task15 = minimalTasksClass.groupAnagrams(Array("eat", "tea", "tan", "ate", "nat", "bat"))
      println(s"TASK 15: Group Anagrams $task15 \n")

*/
    // WEEK 3 - an actor is born

    //first minimal task

    /*val system = ActorSystem("PrinterSystem")
    val printer = system.actorOf(Props[PrinterActor], "printer")

    printer ! "Hello, world!"
    printer ! 42
    printer ! List(1, 2, 3)

    system.terminate()*/


    /*//second minimal task
    val system = ActorSystem("MySystem")
    val myActor = system.actorOf(Props[ModifierActor], "myActor")

    myActor ! 10
    myActor ! "Hello"
    myActor ! (10, "Hello")

    Thread.sleep(1000) // wait for the actor to finish processing

    system.terminate()*/

  /*//third minimal task
  val system = ActorSystem("System")

    val monitoredActor = system.actorOf(Props[MonitoredActor], "monitoredActor")
    val monitoringActor = system.actorOf(Props(new MonitoringActor(monitoredActor)), "monitoringActor")

    monitoredActor ! "Hello"
    monitoredActor ! "World"
    monitoredActor ! PoisonPill

    system.terminate()*/

  //fourth minimal task
    val system = ActorSystem("AveragerSystem")

    val averager = system.actorOf(Props[Averager], name = "Averager")

    println("Current average is 0")
    averager ! 10
    averager ! 10
    averager ! 10

    /*//first main task

    val Pid = new_queue()
    push(Pid, 4)
    push(Pid, 5)
    pop(Pid)
    pop(Pid)
*/
  }

  def new_queue(): ActorRef = {
    val system = ActorSystem("NewQueueActor")
    return system.actorOf(Props[QueueActor], "queueActor")
  }

  def push(actor: ActorRef, number: Int): Unit = {
    actor ! number
  }

  def pop(actor: ActorRef): Unit = {
    actor ! "pop"
  }
}