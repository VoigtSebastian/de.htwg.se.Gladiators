package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.controllerComponent.{GameStatus, MoveGladiatorCommand}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.gladiators.model.GladiatorFactory
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.model.Cell
import de.htwg.se.gladiators.model.CellType
import de.htwg.se.gladiators.util.UndoManager

class UndoManagerSpec extends WordSpec with Matchers {
  val controller = new Controller()
  controller.playingField = createPlayingField(controller.players(0), controller.players(1))

  val moveCommand = new MoveGladiatorCommand(0, 1, 1, 0, controller)
  "A Move GladiatorCommand" when {
    "moving a Gladiator" in {
      controller.endTurn()
      controller.gameStatus = GameStatus.P1
      controller.undoManager.doStep(moveCommand)
      controller.playingField.gladiatorPlayer1.head.row should be(0)
    }
    "undoing that step" in {
      controller.undoManager.undoStep
      controller.playingField.gladiatorPlayer1.head.row should be(1)
    }
    // Todo: Find bug in implementation
    // "redoing that step" in {
    //   controller.redoGladiator()
    //   controller.playingField.gladiatorPlayer1.head.row should be(0)
    // }
  }
  "A empty manager" when {
    val manager = new UndoManager()
    "being called should do nothing" in {
      manager.redoStep
      manager.undoStep
    }
  }
  def createPlayingField(playerOne: Player, playerTwo: Player): PlayingField = {
    val cells = Array.ofDim[Cell](3, 3)
    cells(0)(0) = Cell(CellType.BASE)
    cells(0)(1) = Cell(CellType.SAND)
    cells(0)(2) = Cell(CellType.PALM)
    cells(1)(0) = Cell(CellType.SAND)
    cells(1)(1) = Cell(CellType.SAND)
    cells(1)(2) = Cell(CellType.PALM)
    cells(2)(0) = Cell(CellType.SAND)
    cells(2)(1) = Cell(CellType.SAND)
    cells(2)(2) = Cell(CellType.BASE)
    /*
    0 1 2
    0 B S P
    1 S S P
    2 S S B
    */

    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"

    var playingField = PlayingField(size=3)
    playingField = playingField.updateCells(cells)

    var glad1 = GladiatorFactory.createGladiator(0, 1, GladiatorType.SWORD, playerOne)
    var glad2 = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, playerTwo)


    playingField = playingField.addGladPlayerOne(glad1)
    playingField = playingField.addGladPlayerTwo(glad2)

    playingField
  }
}
