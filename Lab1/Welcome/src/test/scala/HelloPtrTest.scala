import org.scalatest.funsuite.AnyFunSuite
import Lab1.MinimalTasksClass
class HelloPtrTest extends  AnyFunSuite {
  test("HelloPtr.hello") {
    val m = new MinimalTasksClass
    assert(m.helloPTR() == "Hello PTR")
  }
}


