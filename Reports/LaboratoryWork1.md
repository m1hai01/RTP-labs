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

In this implementation, we use a result list to store the elements of the list after removing the consecutive duplicates. We also use a variable lastElement to keep track of the last element we added to the result list. In the for loop, we iterate over each element in the input list. If the current element is different from the last element, we add it to the result list and update the lastElement variable.



## Bibliography

- Installation guide to install [Scala](https://docs.scala-lang.org/getting-started/index.html).
- Scala [cheatsheet](https://docs.scala-lang.org/cheatsheets/index.html).
- Scala [Basics](https://docs.scala-lang.org/tour/basics.html).