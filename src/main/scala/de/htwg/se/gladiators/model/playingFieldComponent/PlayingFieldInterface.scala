package de.htwg.se.gladiators.model.playingFieldComponent

import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.model.{Cell, Gladiator}

import scala.util.matching.Regex

trait PlayingFieldInterface {

  var gladiatorPlayer1: List[Gladiator]
  var gladiatorPlayer2: List[Gladiator]
  var cells: Array[Array[Cell]]
  var toggleUnitStats: Boolean

  val SAND_BACKGROUND: String
  val PALM_BACKGROUND: String
  val BASE_BACKGROUND: String
  val UNIT_BACKGROUND: String
  val TEXT_COLOR_BLACK: String
  val RESET_ANSI_ESCAPE: String
  val REGEX_COMMANDS: Regex

  def setField(cells: Array[Array[Cell]]): PlayingField

  override def toString: String

  def formatLine(line: Int): String

  def formatLineAddGladiators(gladiator: Gladiator, retString: String, line: Int): String

  def resetGladiatorMoved(): Unit

  def formatPlayingFieldAddStats(playingField: String): String

  def createRandom(length: Int, palmRate: Int = 17): PlayingField

  def addGladPlayerOne(gladiator: Gladiator): PlayingField

  def addGladPlayerTwo(gladiator: Gladiator): PlayingField

  def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): PlayingField

  def getSize: Integer

  def cell(line: Int, row: Int): Cell

  def gladiatorInfo(line: Int, row: Int): String

  def evalPrintLine(line: String): String

  def attack(gladiatorAttack: Gladiator, gladiatorDest: Gladiator): String

}

