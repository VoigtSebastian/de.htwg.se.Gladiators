package de.htwg.se.gladiators.aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.TestImplementation.TestingController
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.controller.GameState
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.model.Player

class ControllerSpec extends AnyWordSpec with Matchers {
    "A Tui" when {
        "parsing commands" should {
            "return true" in {
                val controller = TestingController()
                val tui = Tui(controller)
                tui.processInputLine("quit asd asd") should be(false)
                tui.processInputLine("quit") should be(false)
                tui.processInputLine("quit          vyxc") should be(false)

                controller.commandQueue.dequeue should be(Quit)
                controller.commandQueue.dequeue should be(Quit)
                controller.commandQueue.dequeue should be(Quit)
            }
            "return false" in {
                val controller = TestingController()
                controller.gameState = GameState.TurnPlayerOne
                val tui = Tui(controller)

                val stream = new java.io.ByteArrayOutputStream()
                Console.withOut(stream) {
                    tui.processInputLine("ThisIsNotACommand") should be(true)
                    tui.processInputLine("move 1 1 4 4") should be(true)
                    tui.processInputLine("move 1 1 4 4 1234") should be(true)
                    tui.processInputLine("move 2") should be(true)
                }
                controller.commandQueue.dequeue should be(Move(Coordinate(1, 1), Coordinate(4, 4)))
            }
            "send out naming commands" in {
                val controller = TestingController()
                val tui = Tui(controller)
                controller.gameState = NamingPlayerOne
                tui.processInputLine(tui.namePlayerOneMessage + "helmut")

                controller.gameState = NamingPlayerTwo
                tui.processInputLine(tui.namePlayerTwoMessage + "torsten")

                controller.commandQueue.dequeue() should be(NamePlayerOne("helmut"))
                controller.commandQueue.dequeue() should be(NamePlayerTwo("torsten"))
            }

            "end the current turn" in {
                val controller = TestingController()
                controller.gameState = GameState.TurnPlayerOne

                val tui = Tui(controller)

                tui.processInputLine("end")
                tui.processInputLine("e")
                controller.commandQueue.dequeue() should be(EndTurn)
                controller.commandQueue.dequeue() should be(EndTurn)
            }

            "print messages " in {
                val controller = TestingController()
                val tui = Tui(controller)
                val stream = new java.io.ByteArrayOutputStream()
                Console.withOut(stream) {
                    controller.publish(Init)
                    controller.publish(PlayerOneNamed("torsten"))
                    controller.publish(PlayerTwoNamed("torsten"))
                    controller.publish(Turn(Player("", 0, 0, Vector())))

                    tui.processInputLine("shop")
                    tui.processInputLine("s")
                    tui.errorMessage("message")
                    tui.couldNotParseCommand("command")
                }
            }
        }
    }
}
