import Lab1.MinimalTasksClass

import scala.collection.mutable.ListBuffer

object main {
  def main(args: Array[String]): Unit = {
    println("Hello PTR \n");

    val minimalTasksClass = new MinimalTasksClass

    //TASK 1
    val num = 17
    val result = minimalTasksClass.isPrime(num)
    println(s"TASK 1: $result $num is a prime number. \n")

    //TASK 2
    val result1 = minimalTasksClass.cylinderArea(3,4)
    println(s"TASK 2: $result1 is cylinder area \n")


    //TASK 3
    val list = List(1 , 2 , 4 , 8 , 4)
    val result2 = minimalTasksClass.reverse(list)
    println(s"TASK 3: $result2 is reverse of list \n")


    //TASK 4
    val list1 = List(1 , 2 , 4 , 8 , 4)
    val result3 = minimalTasksClass.uniqueSum(list1)
    println(s"TASK 4: $result3 is sum  of unique elements in list\n")

    //TASK 5
    val list2 = ListBuffer(1 , 2 , 4 , 8 , 4)
    val result4 = minimalTasksClass.extractRandomN(list2,3)
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
    val small1 = minimalTasksClass.smallestNumber(4 ,5 ,3)
    val small2 = minimalTasksClass.smallestNumber(0 ,3 ,4)
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
    println(s"TASK 11: Eliminates consecutive duplicates in a $lst1 : $res1")
  }
}
