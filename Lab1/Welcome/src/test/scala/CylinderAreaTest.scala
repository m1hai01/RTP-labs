import org.scalatest.funsuite.AnyFunSuite
import Lab1.MinimalTasksClass

class CylinderAreaTest extends AnyFunSuite {

  test("CylinderArea.area") {
    val m = new MinimalTasksClass
    assert(m.cylinderArea(2, 3) == 94.24777960769379)
  }
}
