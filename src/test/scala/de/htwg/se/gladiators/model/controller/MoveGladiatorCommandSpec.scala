package de.htwg.se.gladiators.model.controller

import de.htwg.se.gladiators.controller.controllerComponent.{GameStatus, MoveGladiatorCommand}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import org.scalatest.{Matchers, WordSpec}

class MoveGladiatorCommandSpec extends WordSpec with Matchers {

  var controller = new Controller()
  controller.createRandom(15, 0)
  controller.addGladiator(13, 7)
  val command = new MoveGladiatorCommand(13, 7, 13, 8, controller)
  "A MoveGladiatorcommand" when {
    "create a move command" in {

      controller.gameStatus = GameStatus.P1
      command.doStep
      controller.playingField.gladiatorPlayer1.head.row should be (8)
    }
    "undo that step " in {
      controller.gameStatus = GameStatus.P1
      command.undoStep
      controller.playingField.gladiatorPlayer1.head.row should be (7)
    }
    "redo that step again " in {
      controller.gameStatus = GameStatus.P1
      command.redoStep
      controller.playingField.gladiatorPlayer1.head.row should be (8)
    }
  }
}
