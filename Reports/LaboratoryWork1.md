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
It is a unit test for Cylinder Area Task

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


## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).
