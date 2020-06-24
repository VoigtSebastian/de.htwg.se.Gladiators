package de.htwg.se.gladiators.model.util

import de.htwg.se.gladiators.controller.controllerComponent.{
    GameStatus,
    MoveGladiatorCommand
}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.model.GladiatorFactory
import de.htwg.se.gladiators.model.GladiatorType

class UndoManagerSpec extends AnyWordSpec with Matchers {
    "A MoveGladiatorcommand" when {
        var controller = new Controller()
        controller.createRandom(15, 0)
        controller.playingField = controller.playingField.addGladPlayerOne(GladiatorFactory.createGladiator(14, 6, GladiatorType.SWORD, controller.players(0)))

        "creating a move command" in {
            controller.playingField.gladiatorPlayer1 should not be empty

            controller.endTurn()
            controller.endTurn()
            controller.gameStatus = GameStatus.P1
            controller.undoManager.doStep(
                new MoveGladiatorCommand(14, 6, 13, 6, controller))
            controller.playingField.gladiatorPlayer1.head.line should be(13)
        }
        "undoing that step " in {
            controller.gameStatus = GameStatus.P1
            controller.undoGladiator()
            controller.playingField.gladiatorPlayer1.head.line should be(14)
        }
        "redoing that step again " in {
            controller.gameStatus = GameStatus.P1
            controller.redoGladiator()
            controller.playingField.gladiatorPlayer1.head.line should be(13)
        }
    }
}
