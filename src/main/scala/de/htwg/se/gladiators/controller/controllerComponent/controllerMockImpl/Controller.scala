package de.htwg.se.gladiators.controller.controllerComponent.controllerMockImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.gladiators.GladiatorsModule
import de.htwg.se.gladiators.controller.controllerComponent.CommandStatus._
import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.{GameStatus, P1, P2}
import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

class Controller @Inject() (val playingField: PlayingField) extends ControllerInterface with Publisher {

    val undoManager = new UndoManager
    var gameStatus: GameStatus = GameStatus.P1
    var commandStatus: CommandStatus = CommandStatus.IDLE
    var players = Array(Player("Player1"), Player("Player2"))
    var selectedCell: (Int, Int) = (0, 0)
    var selectedGlad: Gladiator = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
    var shop = Shop(10)
    val injector = Guice.createInjector(new GladiatorsModule)

   // val playingField = PlayingField()

    def cell(line: Int, row: Int): Cell = playingField.cell(line, row)

    def resetGame(): Unit = {}

    def endTurn(): String = ""

    def createRandom(size: Int, palmRate: Int = 17): Unit = {}

    def getShop: String = ""

    def printPlayingField(): String = ""

    def addGladiator(line: Int, row: Int): Boolean = false

    def buyGladiator(index: Int, line: Int, row: Int): String = ""

    def baseArea(player: Player): List[(Int, Int)] = List()


    def getBase(player: Player): (Int, Int) = (0, 0)

    def gladiatorInfo(line: Int, row: Int): String = {
        playingField.gladiatorInfo(line: Int, row: Int) + " and is owned by " + players(gameStatus.id)
    }

    def isCoordinateLegal(line: Int, row: Int): Boolean = false

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): (Boolean, String) = (false, "")

    def toggleUnitStats(): Unit = {}

    def undoGladiator(): Unit = {}

    def nextPlayer(): Unit = {}

    def redoGladiator(): Unit = {}

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): (Boolean, String) = (false, "")

    def checkGladiator(line: Int, row: Int): Boolean = false

    def getGladiator(line: Int, row: Int): Gladiator = GladiatorFactory.createGladiator(0, 0, GladiatorType.SWORD, Player(""))

    def getGladiatorOption(line: Int, row: Int): Option[Gladiator] = None

    def cellSelected(line: Int, row: Int): Unit = {}

    override def changeCommand(commandStatus: CommandStatus): ControllerInterface = this

    def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType = MoveType.ILLEGAL_MOVE

    def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean = false

    def checkMovementPoints(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = false

    def checkMovementPointsAttack(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = false

    def mineGold(gladiatorAttack: Gladiator, line: Int, row: Int): String = ""

    def checkCellEmpty(line: Int, row: Int): Boolean = false
}
