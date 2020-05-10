package de.htwg.se.gladiators.model.playingFieldComponent

import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import de.htwg.se.gladiators.model.CellType.CellType
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.model.{Cell, Gladiator, Player}
import de.htwg.se.gladiators.util.Coordinate

import scala.util.matching.Regex

trait PlayingFieldInterface {

  val size: Integer
  val gladiatorPlayer1: List[Gladiator]
  val gladiatorPlayer2: List[Gladiator]
  val cells: Array[Array[Cell]]
  var toggleUnitStats: Boolean

  val SAND_BACKGROUND: String
  val PALM_BACKGROUND: String
  val BASE_BACKGROUND: String
  val UNIT_BACKGROUND: String
  val TEXT_COLOR_BLACK: String
  val RESET_ANSI_ESCAPE: String
  val REGEX_COMMANDS: Regex

  def updateCells(cells: Array[Array[Cell]]): PlayingField

  override def toString: String

  def formatLine(line: Int): String

  def formatLineAddGladiators(gladiator: Gladiator, retString: String, line: Int): String

  def resetGladiatorMoved(): PlayingField

  def formatPlayingFieldAddStats(playingField: String): String

  def createRandomCells(length: Int, palmRate: Int = 17): PlayingField

  def addGladPlayerOne(gladiator: Gladiator): PlayingField

  def addGladPlayerTwo(gladiator: Gladiator): PlayingField

  def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): PlayingField

  def setGladiator(line: Int, row: Int, glad: Gladiator): PlayingField

  def getGladiatorOption(position: Coordinate): Option[Gladiator]

  def getSize: Integer

  def cell(line: Int, row: Int): Cell

  def checkCellEmpty(coord: Coordinate): Boolean

  def checkCellWalk(coord: Coordinate): Boolean

  def gladiatorInfo(line: Int, row: Int): String

  def evalPrintLine(line: String): String

  def attack(gladiatorAttack: Gladiator, gladiatorDest: Gladiator): PlayingField

  def resetPlayingField(): PlayingField

  def setCell(line: Int, row: Int, cellType: CellType): Unit

  def checkMoveType(startPosition: Coordinate, destinationPosition: Coordinate, currentPlayer: Player): MoveType

  def checkMovementPointsAttack(g: Gladiator, startPosition: Coordinate, destination: Coordinate): Boolean

  def checkMovementPointsMove(g: Gladiator, startPosition: Coordinate, destination: Coordinate): Boolean

  def getValidMoveCoordinates(g: Gladiator, startPosition: Coordinate): List[Coordinate]

  def getValidMoveCoordinatesHelper(curr: Coordinate, dist: Int, maxDist: Double, validCells: List[(Coordinate, Int)]): List[(Coordinate, Int)]
}

