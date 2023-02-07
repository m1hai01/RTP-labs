import Lab1.MinimalTasksClass

import scala.collection.mutable.ListBuffer

object main {
  def main(args: Array[String]): Unit = {
    println("Hello PTR");

    val minimalTasksClass = new MinimalTasksClass

    //TASK 1
    val num = 17
    val result = minimalTasksClass.isPrime(num)
    println(s"$result $num is a prime number.")

    //TASK 2
    val result1 = minimalTasksClass.cylinderArea(3,4)
    println(s"$result1 is cylinder area")


    //TASK 3
    val list = List(1 , 2 , 4 , 8 , 4)
    val result2 = minimalTasksClass.reverse(list)
    println(s"$result2 is reverse of list")


    //TASK 4
    val list1 = List(1 , 2 , 4 , 8 , 4)
    val result3 = minimalTasksClass.uniqueSum(list1)
    println(s"$result3 is sum  of unique list")

    //TASK 5
    val list2 = ListBuffer(1 , 2 , 4 , 8 , 4)
    val result4 = minimalTasksClass.extractRandomN(list2,3)
    println(s"$result4 randomly selected elements  from a list")

    //TASK 6
     val result5 = minimalTasksClass.firstFibonacciElements(7)
     println(s"$result5 is the first 7 Fibonacci ELements")

    //TASK 7

    val dictionary = Map("mama" -> "mother", "papa" -> "father")
    val originalString = "mama is with papa"
    val translatedString = minimalTasksClass.translator(dictionary, originalString)
    println(s"Original string: $originalString")
    println(s"Translated string: $translatedString")

  //TASK 8

    val small1 = minimalTasksClass.smallestNumber(4 ,5 ,3)
    val small2 = minimalTasksClass.smallestNumber(0 ,3 ,4)
    println(s"First ex: $small1")
    println(s"Second ex: $small2")


    //TASK 9
    val lst = List(1, 2, 4, 8, 4)
    val rotatedList = minimalTasksClass.rotateLeft(lst, 3)
    println(rotatedList)

    //TASK 10
    val rs = minimalTasksClass.listRightAngleTriangles()
    println(rs)
    //TASK 11
    val lst1 = List(1, 2, 2, 2, 4, 8, 4)
    val res1 = minimalTasksClass.removeConsecutiveDuplicates(lst1)
    println(lst1)
  }
}
