package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.controller.TestImplementation.TestingController
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Events._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TestingControllerSpec extends AnyWordSpec with Matchers {
    "A Testing Controller" when {
        "receiving commands" should {
            "save those commands" in {
                val controller = TestingController()
                controller.inputCommand(Quit)
                controller.commandQueue.dequeue() should be(Quit)
            }
        }
        "receiving function calls" should {
            val controller = TestingController()
            "save a Move command in " in {
                controller.move(Coordinate(0, 0), Coordinate(0, 0)).isInstanceOf[ErrorMessage] should be(true)
                controller.commandQueue.dequeue() should be(Move(Coordinate(0, 0), Coordinate(0, 0)))
            }
            "save a NamePlayerOne command in " in {
                controller.namePlayerOne("Karl").isInstanceOf[ErrorMessage] should be(true)
                controller.commandQueue.dequeue() should be(NamePlayerOne("Karl"))
            }
            "save a NamePlayerTwo command in " in {
                controller.namePlayerTwo("Klößchen").isInstanceOf[ErrorMessage] should be(true)
                controller.commandQueue.dequeue() should be(NamePlayerTwo("Klößchen"))
            }
            "save a EndTurn command in " in {
                controller.endTurn.isInstanceOf[ErrorMessage] should be(true)
                controller.commandQueue.dequeue() should be(EndTurn)
            }
            "save a BuyUnit command in " in {
                controller.buyUnit(0, Coordinate(0, 0)).isInstanceOf[ErrorMessage] should be(true)
                controller.commandQueue.dequeue() should be(BuyUnit(0, Coordinate(0, 0)))
            }
            "return false when being asked if this tile is occupied" in {
                ((1 to 10).zip(1 to 10)).foreach({ case (x, y) => controller.tileOccupiedByCurrentPlayer(Coordinate(x, y)) should be(false) })
            }
            "return None when asking for attacking tiles at a certain position" in {
                ((1 to 10).zip(1 to 10)).foreach({ case (x, y) => controller.attackTiles(Coordinate(x, y)) should be(None) })
            }
            "return None when asking for moving tiles at a certain position" in {
                ((1 to 10).zip(1 to 10)).foreach({ case (x, y) => controller.moveTiles(Coordinate(x, y)) should be(None) })
            }
            "return None when asking for new unit placement" in {
                controller.newUnitPlacementTiles should be(None)
            }
            "return an empty Vector when being asked for board tiles" in {
                controller.boardTiles should be(Vector.empty)
            }
            "return None when being asked for the Gladiators of Player One" in {
                controller.gladiatorsPlayerOne should be(None)
            }
            "return None when being asked for the Gladiators of Player Two" in {
                controller.gladiatorsPlayerTwo should be(None)
            }
            "return an empty list when being asked for the Gladiators in stock" in {
                controller.stock should be(Vector.empty)
            }
            "return a ??? string when being asked for a board string representation" in {
                controller.boardToString should be("???")
            }
        }
    }
}
