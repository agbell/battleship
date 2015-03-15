import akka.testkit.{TestActorRef, TestFSMRef, TestKit}
import akka.util.Timeout
import com.mildlyskilled.actors.{Ship, Cell}
import com.mildlyskilled.messages._
import com.mildlyskilled.states.{Vacant, Inactive, Occupied}

import scala.concurrent.duration._

class CellTestSuite extends BattleShipTestHarness {

  implicit val timeout = Timeout(5 seconds)

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  val fsm = TestFSMRef(new Cell(10, 10))
  val cellActor: TestActorRef[Cell] = fsm

  val shipActor = TestActorRef(new Ship(3))

  "A cell actor" must {

    "On initialization be vacant" in {
      assert(fsm.stateName == Vacant)
    }

    "place ship" in {
      cellActor ! PlaceShip(shipActor)

      //cell state occupied, data = ship ctor
      assert(fsm.stateName == Occupied)
      assert(fsm.stateData == PlaceShip(shipActor))
    }

    "fire on occupied cell" in {
      cellActor ! Fire(10, 10)

      //cell state occupied, data = ship ctor
      assert(fsm.stateName == Inactive)
      assert(fsm.stateData == PlaceShip(shipActor))
    }

  }
}
