package de.htwg.se.gladiators.controller.controllerComponent


import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import CommandStatus.CommandStatus
import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.model.fileIoComponent.FileIOInterface
import de.htwg.se.gladiators.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.util.UndoManager
import de.htwg.se.gladiators.util.Coordinate



import scala.swing.Publisher

trait ControllerInterface extends Publisher {

    var playingField: PlayingFieldInterface
    val undoManager: UndoManager
    var gameStatus: GameStatus
    var commandStatus: CommandStatus
    var players: Array[Player]
    var selectedCell: (Int, Int)
    var selectedGlad: Gladiator
    var shop: Shop
    var fileIo: FileIOInterface

    def cell(line: Int, row: Int): Cell

    /**
      * Resets the game and return a reset ControllerInterface implementation.
      *
      * @return a reset ControllerInterface implementation
      */
    def resetGame(): Unit

    /**
      * Ends the turn for the current player.
      *
      * @return String containing a message that can be shown in a Tui.
      */
    def endTurn(): String

    /**
      * Creates a random PlayingField, giving all cells a new CellType.
      *
      * @param size     : The size of the new playing field.
      * @param palmRate : The rate at which palm should appear between 0 and 100.
      */
    def createRandom(size: Int, palmRate: Int = 17): Unit

    /**
      * Returns the current shop.
      *
      * @return current shop.
      */
    def getShop: String

    def printPlayingField(): String

    def addGladiator(line: Int, row: Int): Boolean

    def buyGladiator(index: Int, line: Int, row: Int): String

    def baseArea(player: Player): List[(Int, Int)]

    def getBase(player: Player): (Int, Int)

    def gladiatorInfo(line: Int, row: Int): String

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): (Boolean, String)

    def toggleUnitStats(): Unit

    def undoGladiator(): Unit

    def nextPlayer(): Unit

    def redoGladiator(): Unit

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): (Boolean, String)

    def getGladiator(line: Int, row: Int): Gladiator

    def getGladiatorOption(position: Coordinate): Option[Gladiator]

    def cellSelected(line: Int, row: Int): Unit

    def changeCommand(commandStatus: CommandStatus): ControllerInterface

    def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType

    def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean

    def checkMovementPointsAttack(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean

    def mineGold(gladiatorAttack: Gladiator, line: Int, row: Int): String

    def save: Unit

    def load: Unit

    def playingFieldToHtml: String
}
