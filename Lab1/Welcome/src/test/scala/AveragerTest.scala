package ActorLab.Minimal4

import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.{EventFilter, ImplicitSender, TestActors, TestKit}
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class AveragerTest() extends TestKit(ActorSystem("AveragerSystem"))
  with AnyWordSpecLike
  with ImplicitSender
  with Matchers
  with BeforeAndAfterAll
  {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
    "Averager" must {
    "correctly calculate average for multiple integers" in {
      val averager = system.actorOf(Props[Averager])

      averager ! 10
      expectMsg("Current average is 5.0")
      //expectNoMessage()
      averager ! 10
      expectMsg("Current average is 7.5")
      averager ! 10
      expectMsg("Current average is 8.75")
    }
  }
}

