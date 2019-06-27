package de.htwg.se.gladiators.controller.controllerComponent





import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import CommandStatus.CommandStatus
import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

trait ControllerInterface extends Publisher {

  var playingField: PlayingField
  val undoManager : UndoManager
  var gameStatus: GameStatus
  var commandStatus: CommandStatus
  var players : Array[Player]
  var selectedCell: (Int, Int)
  var selectedGlad: Gladiator
  var shop: Shop

  def cell(line: Int, row: Int): Cell

  def resetGame(): Controller
  def endTurn(): String
  def createRandom(size: Int, palmRate: Int = 17): Unit

  def getShop: String

  def printPlayingField(): String

  def addGladiator(line: Int, row: Int): Boolean
  def buyGladiator(index: Int, line: Int, row: Int): String
  def baseArea(player: Player): List[(Int, Int)]
  def getBase(player: Player): (Int, Int)

  def gladiatorInfo(line: Int, row: Int): String
  def isCoordinateLegal(line: Int, row: Int): Boolean

  def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): (Boolean, String)

  def toggleUnitStats(): Unit
  def undoGladiator(): Unit

  def nextPlayer(): Unit
  def redoGladiator(): Unit
  def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): (Boolean, String)

  def checkGladiator(line: Int, row: Int): Boolean

  def getGladiator(line: Int, row: Int): Gladiator

  def getGladiatorOption(line: Int, row: Int): Option[Gladiator]
  def cellSelected(line: Int, row: Int): Unit

  def changeCommand(commandStatus: CommandStatus): Controller

  def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType
  def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean

  def checkMovementPoints(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean

  def checkMovementPointsAttack(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean
  def mineGold(gladiatorAttack: Gladiator, line: Int, row: Int): String
  def checkCellEmpty(line: Int, row: Int): Boolean

}
