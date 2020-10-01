package de.htwg.se.gladiators.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.TestImplementation.TestingController
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Command._

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
        }
    }
}
