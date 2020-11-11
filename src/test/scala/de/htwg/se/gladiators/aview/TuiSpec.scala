package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.GameState
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.controller.TestImplementation.TestingController
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Factories.GladiatorFactory

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

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
                    controller.publish(Turn(Player("", 0, 0, 100, false)))
                    controller.publish(ErrorMessage("error"))
                    controller.publish(SuccessfullyBoughtGladiator(Player("", 0, 0, 100, false), GladiatorFactory.createGladiator()))
                    controller.publish(Moved(Player("", 0, 0, 100, false), Coordinate(0, 0), Coordinate(0, 0), GladiatorFactory.createGladiator()))
                    controller.publish(Shutdown)
                    controller.publish(Won(Player("", 0, 0, 0, false)))
                    controller.publish(Attacked(Player("", 0, 0, 0, false), true, Coordinate(0, 0), Coordinate(0, 0)))
                    controller.publish(Mined(Player("", 0, 0, 0, false), 10, true))

                    controller.gameState = TurnPlayerOne

                    tui.processInputLine("shop")
                    tui.processInputLine("s")
                    tui.errorMessage("message")
                    tui.couldNotParseCommand("command")
                }
            }
            "send out buy commands" in {
                val controller = TestingController()
                controller.gameState = TurnPlayerOne
                val tui = Tui(controller)
                tui.processInputLine("buy 1 10 10")
                tui.processInputLine("b 1 10 10")

                controller.commandQueue.dequeue().isInstanceOf[BuyUnit] should be(true)
                controller.commandQueue.dequeue().isInstanceOf[BuyUnit] should be(true)
            }
        }
    }
}
