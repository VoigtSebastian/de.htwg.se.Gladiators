package de.htwg.se.gladiators.model.util

import de.htwg.se.gladiators.controller.controllerComponent.{
  GameStatus,
  MoveGladiatorCommand
}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class UndoManagerSpec extends AnyWordSpec with Matchers {

  var controller = new Controller()
  controller.createRandom(15, 0)
  controller.addGladiator(13, 7)

  val command = new MoveGladiatorCommand(5, 3, 5, 4, controller)
  "A MoveGladiatorcommand" when {
    "creating a move command" in {

      controller.gameStatus = GameStatus.P1
      controller.undoManager.doStep(
        new MoveGladiatorCommand(13, 7, 13, 8, controller)
      )
      controller.playingField.gladiatorPlayer1.head.row should be(8)
    }
    "undoing that step " in {
      controller.gameStatus = GameStatus.P1
      controller.undoGladiator()
      controller.playingField.gladiatorPlayer1.head.row should be(7)
    }
    "redoing that step again " in {
      controller.gameStatus = GameStatus.P1
      controller.redoGladiator()
      controller.playingField.gladiatorPlayer1.head.row should be(8)
    }
  }
}
