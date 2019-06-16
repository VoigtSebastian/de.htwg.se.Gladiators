package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.controller.CommandStatus._
import de.htwg.se.gladiators.controller.GameStatus._
import de.htwg.se.gladiators.controller.MoveType.MoveType
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

class Controller(var playingField: PlayingField) extends Publisher {

    private val undoManager = new UndoManager
    val DIMENSIONS = 7
    var gameStatus: GameStatus = GameStatus.P1
    var commandStatus: CommandStatus = CommandStatus.IDLE
    var players = Array(Player("Player1"), Player("Player2"))
    var selectedCell: (Int, Int) = (0, 0)
    var selectedGlad: Gladiator = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
    val shop = new Shop(10)

    def createRandom(): Unit = {
        playingField = playingField.createRandom(playingField.size)
        //notifyObservers
        publish(new PlayingFieldChanged)
    }

    def getShop: String = shop.toString


    def printPlayingField(): String = {
        // notifyObservers
        playingField.toString +
          GameStatus.message(gameStatus) +
          "\n"
    }

    def addGladiator(line: Int, row: Int): Unit = {
        if (playingField.cells(line)(row).cellType == CellType.PALM)
            return

        if (checkGladiator(line, row))
            return

        if (!baseArea(players(gameStatus.id)).contains((line,row)))
            return

        if (selectedGlad.line == -2) {
            for((g,i) <- shop.stock.zipWithIndex) {
                if(g == selectedGlad) {
                    shop.buy(i, players(gameStatus.id))
                }
            }
            selectedGlad.line = line
            selectedGlad.row = row
            selectedGlad.player = players(gameStatus.id)
            if (gameStatus == P1)
                playingField = playingField.addGladPlayerOne(selectedGlad)
            else if (gameStatus == P2)
                playingField = playingField.addGladPlayerTwo(selectedGlad)

        } else {
            selectedGlad = shop.genGlad()
            selectedGlad.line = line
            selectedGlad.row = row
            selectedGlad.player = players(gameStatus.id)
            if (gameStatus == P1)
                playingField = playingField.addGladPlayerOne(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
            else if (gameStatus == P2)
                playingField = playingField.addGladPlayerTwo(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
        }

       // players(gameStatus.id).buyItem(10)

        nextPlayer()
        publish(new GladChanged)
        //playingField
    }

    def baseArea(player: Player): List[(Int, Int)] = {
        var base1: (Int, Int) = (0,0)
        var area: List[(Int, Int)] = Nil
        if(player == players(0)) {
            base1 = (playingField.size - 1, playingField.size / 2)
            area = area ::: (base1._1 - 1, base1._2) :: Nil
        } else if (player == players(1)) {
            base1 = (0, playingField.size / 2)
            area = area ::: (base1._1 + 1, base1._2) :: Nil
        }

        area = area ::: (base1._1, base1._2 - 1) :: Nil
        area = area ::: (base1._1, base1._2 + 1) :: Nil

        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            area = area.filter(c => !(c._1 == g.line && c._2 == g.row))
        }
        area.filter(c => playingField.cells(c._1)(c._2).cellType != CellType.PALM)
    }

    def gladiatorInfo(line: Int, row: Int): String = {
        playingField.gladiatorInfo(line: Int, row: Int) + " and is owned by " + players(gameStatus.id)
    }

    def isCoordinateLegal(line: Int, row: Int): Boolean = {
        if (line < DIMENSIONS && line >= 0 && row < DIMENSIONS && row >= 0)
            return true
        false
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): String = {
        val status: MoveType.MoveType = categorizeMove(line, row, lineDest, rowDest)

        status match {
            case MoveType.ATTACK => MoveType.message(status) //TODO: Change this behaviour?
            case MoveType.LEGAL_MOVE =>
                undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
                nextPlayer()
                publish(new GladChanged)
                "Move successful!"
            case _ => MoveType.message(status)
        }
    }

    def toggleUnitStats(): Unit = {
        if (playingField.toggleUnitStats)
            playingField.toggleUnitStats = false
        else
            playingField.toggleUnitStats = true
    }

    def undoGladiator(): Unit = {
        undoManager.undoStep
    }

    def nextPlayer(): Unit = {
        if (gameStatus == P1)
            gameStatus = P2
        else
            gameStatus = P1
        publish(new GameStatusChanged)
    }

    def cell(line: Int, row: Int): Cell = playingField.cell(line, row)


    def redoGladiator(): Unit = {
        undoManager.redoStep
    }

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): String = {
        val status: MoveType.MoveType = categorizeMove(lineAttack, rowAttack, lineDest, rowDest)

        status match {
            case MoveType.ATTACK => nextPlayer(); playingField.attack(getGladiator(lineAttack, rowAttack), getGladiator(lineDest, rowDest))
            case MoveType.LEGAL_MOVE => "Please use the move command to move your units"
            case MoveType.ILLEGAL_MOVE => MoveType.message(status)
            case MoveType.BLOCKED => "You can not attack your own units"
            case _ => MoveType.message(status)
        }
    }

    def checkGladiator(line: Int, row: Int): Boolean = {
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                return true
        }
        false
    }

    def getGladiator (line: Int, row: Int): Gladiator = {
        var glad = GladiatorFactory.createGladiator(Int.MinValue, Int.MinValue, GladiatorType.SWORD, players(P1.id))
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                glad = g
        }
        glad
    }

    def getGladiatorOption(line: Int, row: Int): Option[Gladiator] = {
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                return Some(g)
        }
        None
    }

    def cellSelected(line: Int, row: Int): Unit = {
        if (commandStatus == CommandStatus.IDLE) {
            selectedCell = (line, row)
            publish(new CellClicked)
        } else if (commandStatus == CommandStatus.CR) {
            addGladiator(line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        } else if (commandStatus == CommandStatus.MV) {
            moveGladiator(selectedCell._1, selectedCell._2, line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        } else if (commandStatus == CommandStatus.AT) {
            attack(selectedCell._1, selectedCell._2, line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        }
    }

    def changeCommand(commandStatus: CommandStatus): Unit = {
        this.commandStatus = commandStatus
    }

    def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType = {
        if (!isCoordinateLegal(lineStart, rowStart) ||
            !isCoordinateLegal(lineDest, rowDest))
            return MoveType.ILLEGAL_MOVE

        val gladStartOption = getGladiatorOption(lineStart, rowStart)
        val gladDestOption = getGladiatorOption(lineDest, rowDest)

        gladStartOption match {
            case Some(gladiatorStart) =>
                if (gladiatorStart.player != players(gameStatus.id))
                    return MoveType.UNIT_NOT_OWNED_BY_PLAYER

                gladDestOption match {
                    case Some(gladiatorDest) =>
                        if (gladiatorStart.player == gladiatorDest.player)
                            return MoveType.BLOCKED
                        if (checkMovementPoints(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                            MoveType.ATTACK
                        else
                            MoveType.INSUFFICIENT_MOVEMENT_POINTS
                    case None =>
                        if (playingField.cells(lineDest)(rowDest).cellType != CellType.PALM)
                            if (checkMovementPoints(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                                MoveType.LEGAL_MOVE
                            else
                                MoveType.INSUFFICIENT_MOVEMENT_POINTS
                        else
                            MoveType.MOVE_TO_PALM
                }
            case None => MoveType.UNIT_NOT_EXISTING
        }
    }

    def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean = {
        for (g <- list)
            if (g.row == row && g.line == line)
                return true
        false
    }

    def checkMovementPoints(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = {
        if (g.row == rowStart &&
            g.line == lineStart &&
            g.movementPoints >= (Math.abs(lineDest - lineStart) + Math.abs(rowDest - rowStart)))
            return true
        false
    }

    def checkMovementPoints(list: List[Gladiator], lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = {
        for (g <- list)
            if (g.row == rowStart &&
                g.line == lineStart &&
                g.movementPoints >= (Math.abs(lineDest - lineStart) + Math.abs(rowDest - rowStart)))
                return true
        false
    }
}
