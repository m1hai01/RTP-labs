package Lab1
import java.util.Dictionary
import scala.collection.mutable.ListBuffer
import util.Random

class MinimalTasksClass {

  //TASK 1
  def isPrime(n: Int): Boolean = {
      // check if n is divisible by any number in the range 2 until n.
      if (n <= 1) return false
      for (i <- 2 until n) {
        if (n % i == 0) return false
      }
      true
  }

    //TASK 2
    def cylinderArea(h: Double , r: Double): Double = {
      val pi :Double = 3.141592653589793238
      val surface: Double = 2 * pi * r * h + 2 * pi * r * r
      surface
    }

    //TASK 3
    def reverse(s: List[Int]): List[Int] = {
      val result  = s.reverse
      result
    }

    //TASK 4
    def uniqueSum(s: List[Int]): Int = {
      val value = s.distinct
      val sum = value.sum
      sum
    }

    //TASK 5
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

    //TASK 6
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

    //TASK 7
    def translator(dictionary: Map[String, String], originalString: String): String = {
      originalString.split(" ").map(word => dictionary.getOrElse(word, word)).mkString(" ")
    }

    //TASK 8

  def smallestNumber(a: Int, b: Int, c: Int): Int = {
    val numbers = List(a, b, c)
    val sortedNumbers = numbers.sorted
    if (sortedNumbers.head == 0) {
      sortedNumbers(1) * 100 + sortedNumbers.head * 10 + sortedNumbers(2)
    } else {
      sortedNumbers.head * 100 + sortedNumbers(1) * 10 + sortedNumbers(2)
    }
  }

  //TASK 9
  def rotateLeft(s: List[Int], n: Int): List[Int] = {
    val newStartIndex = n % s.length
    s.drop(newStartIndex) ++ s.take(newStartIndex)
  }

  //TASK 10
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
  //TASK 11
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

  //TASK 12
  //Selectam elementele care satisfac conditia de a avea toate caracterele dintr-o linie a tastaturii-filter
  //Verificam daca fiecare element din lista de cuvinte are toate caracterele dintr-o linie a tastaturii-exists
  //Verificam daca toate caracterele dintr-un cuvant sunt continute in linia curenta-forall
  //Verificam daca un caracter se afla in linia curenta-contains
  def lineWords(words: Array[String]): Array[String] = {
    val rows = List("qwertyuiop", "asdfghjkl", "zxcvbnm")
    words.filter(word => rows.exists(row => word.toLowerCase.forall(c => row.contains(c))))
  }

  //TASK 13
  // pentru fiecare caracter din sirul de intrare, se aduna cu n
  // apoi se converteste inapoi la tipul Char
  // se obtine un sir de caractere care este returnat
  //
  def encode(s: String, n: Int): String = {
    s.map(c => (c + n).toChar)
  }
  def decode(s: String, n: Int): String = {
    s.map(c => (c - n).toChar)
  }

  //TASK 14
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

    // avem lista initiala cu valori intermediare care se numeste combinations
    // combinatiile initiale sunt doar un string gol
    // pentru fiecare caracter din sirul de intrare, combinatiile initiale sunt
    // inlocuite cu combinatiile posibile ale caracterului curent
    // combinatiile posibile sunt obtinute prin concatenarea caracterului curent
    // cu fiecare combinatie initiala
    // adica daca avem combinatiile initiale ["a", "b", "c"] si caracterul curent
    // este "d", combinatiile finale vor fi ["ad", "bd", "cd"]
    // este "e" combinatiile finale vor fi ["ae", "be", "ce"]
    // este "f" combinatiile finale vor fi ["af", "bf", "cf"]
    // la urma obtinem combinatiile finale ["ad", "bd", "cd", "ae", "be", "ce", "af", "bf", "cf"]
    s.foldLeft(List("")) { (combinations, s) =>
      for {
        combination <- combinations
        char <- phoneMap(s)
      } yield combination + char
    }
  }
  //TASK 15
  def groupAnagrams(s: Array[String]): Map[String, List[String]] = {
    val groups = s.groupBy(_.sorted)
    groups.view.mapValues(_.toList).toMap
  }


}


