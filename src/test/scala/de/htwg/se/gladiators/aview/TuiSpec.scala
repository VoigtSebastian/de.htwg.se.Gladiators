package de.htwg.se.gladiators.aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.TestImplementation.TestingController
import de.htwg.se.gladiators.util.Command.Quit
import de.htwg.se.gladiators.util.Command.Move
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.controller.GameState

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
                tui.processInputLine("ThisIsNotACommand") should be(true)
                tui.processInputLine("move 1 1 4 4") should be(true)
                tui.processInputLine("move 1 1 4 4 1234") should be(true)
                tui.processInputLine("move 2") should be(true)

                controller.commandQueue.dequeue should be(Move(Coordinate(1, 1), Coordinate(4, 4)))
            }
        }
    }
}
