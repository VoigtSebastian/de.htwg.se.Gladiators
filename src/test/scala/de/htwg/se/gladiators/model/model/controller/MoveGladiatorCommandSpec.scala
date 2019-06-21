package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.{Controller, GameStatus, MoveGladiatorCommand}
import de.htwg.se.gladiators.model.PlayingField
import org.scalatest.{Matchers, WordSpec}

class MoveGladiatorCommandSpec extends WordSpec with Matchers {

  var controller = new Controller(PlayingField(7))
  controller.createRandom(7, 0)
  controller.addGladiator(5, 3)
  val command = new MoveGladiatorCommand(5, 3, 5, 4, controller)
  "A MoveGladiatorcommand" when {
    "create a move command" in {

      controller.gameStatus = GameStatus.P1
      command.doStep
      controller.playingField.gladiatorPlayer1.head.row should be (4)
    }
    "undo that step " in {
      controller.gameStatus = GameStatus.P1
      command.undoStep
      controller.playingField.gladiatorPlayer1.head.row should be (3)
    }
    "redo that step again " in {
      controller.gameStatus = GameStatus.P1
      command.redoStep
      controller.playingField.gladiatorPlayer1.head.row should be (4)
    }
  }
}
