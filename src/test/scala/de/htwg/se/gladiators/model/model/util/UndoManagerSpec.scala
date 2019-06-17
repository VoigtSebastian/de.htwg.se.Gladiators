package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.{Controller, GameStatus, MoveGladiatorCommand}
import de.htwg.se.gladiators.model.PlayingField
import org.scalatest.{Matchers, WordSpec}

class UndoManagerSpec extends WordSpec with Matchers {
  var controller = new Controller(PlayingField())
  controller.createRandom(7, 0)
  controller.addGladiator(5, 3)
  val command = new MoveGladiatorCommand(5, 3, 4, 4, controller)

  "A MoveGladiatorcommand" when {
    "do a move command" in {
      controller.playingField.gladiatorPlayer1.head.line should be (5)
      command.doStep
      controller.gameStatus = GameStatus.P1
      controller.playingField.gladiatorPlayer1.head.line should be (4)
    }
    "undo that step " in {
      command.doStep
      controller.gameStatus = GameStatus.P1
      command.undoStep
      controller.playingField.gladiatorPlayer1.head.line should be (5)
    }
    "redo that step again " in {
      controller.gameStatus = GameStatus.P1
      command.redoStep
      controller.playingField.gladiatorPlayer1.head.line should be (4)
    }
  }
}
