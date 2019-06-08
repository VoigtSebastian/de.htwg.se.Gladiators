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
    var selectedCell: (Int, Int) = (0,0)

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
        //notifyObservers
        publish(new PlayingFieldChanged)
    }

    //def createCommand(command:String): Vector[String] = {
    //    //notifyObservers
    //    TuiEvaluator.evalCommand(command)
    //}

    def printPlayingField(): String = {
        // notifyObservers
        playingField.toString + players(gameStatus.id) + "\n"
    }

    def addGladiator(line: Int, row: Int, gladiatorType: GladiatorType): Unit = {
        if (gameStatus == P1)
            playingField = playingField.addGladPlayerOne(GladiatorFactory.createGladiator(line, row, gladiatorType, players(gameStatus.id)))
        else if (gameStatus == P2)
                playingField = playingField.addGladPlayerTwo(GladiatorFactory.createGladiator(line, row, gladiatorType, players(gameStatus.id)))
        players(gameStatus.id).buyItem(10)
        //notifyObservers
        nextPlayer()
        publish(new GladChanged)
        //playingField
    }

    def gladiatorInfo(line: Int, row: Int): String = {
        playingField.gladiatorInfo(line: Int, row: Int) + " and is owned by " + players(gameStatus.id)
    }

    def isCoordinateLegal(line: Int, row: Int): Boolean = {
        if (line < DIMENSIONS && line >= 0 && row < DIMENSIONS && row >= 0)
            return true
        false
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): Unit = {
        if (isCoordinateLegal(lineDest, rowDest)) {
            undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
            nextPlayer()
            publish(new GladChanged)
        }
    }

    def undoGladiator(): Unit = {
        undoManager.undoStep
    }

    def nextPlayer(): Unit = {
        publish(new GameStatusChanged)
        if (gameStatus == P1)
            gameStatus = P2
        else
            gameStatus = P1
    }

    def cell(line: Int, row: Int): Cell = playingField.cell(line, row)


    def redoGladiator(): Unit = {
        undoManager.redoStep
    }

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): String = {
        val status: MoveType.MoveType = categorizeMove(lineAttack, rowAttack, lineDest, rowDest)

        status match {
            case MoveType.ATTACK        => playingField.attack(getGladiator(lineAttack, rowAttack), getGladiator(lineDest, rowDest))
            case MoveType.LEGAL_MOVE    => "Please use the move command to move your units"
            case MoveType.ILLEGAL_MOVE  => "This move is not possible"
            case _                      => "You can not attack your own units"

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

    def cellSelected(line: Int, row: Int): Unit = {
        if (commandStatus == CommandStatus.IDLE) {
            selectedCell = (line, row)
            publish(new CellClicked)
        } else if (commandStatus == CommandStatus.CR) {
            addGladiator(line, row, GladiatorType.SWORD)
            commandStatus = CommandStatus.IDLE
            publish(new GladChanged)
        } else if (commandStatus == CommandStatus.MV ) {
            moveGladiator(selectedCell._1, selectedCell._2, line, row)
            commandStatus = CommandStatus.IDLE
            publish(new GladChanged)
        }
    }

    def changeCommand(commandStatus: CommandStatus): Unit = {
        this.commandStatus = commandStatus
    }

    def categorizeMove (lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): MoveType = {
        if (!isCoordinateLegal(lineAttack, rowAttack) ||
            !isCoordinateLegal(lineDest, rowDest))
            MoveType.ILLEGAL_MOVE

        if (gameStatus == P1 &&
            isGladiatorInList(playingField.gladiatorPlayer1, lineAttack, rowAttack) &&
            isGladiatorInList(playingField.gladiatorPlayer2, lineDest, rowDest))
            MoveType.ATTACK

        if (gameStatus == P2 &&
            isGladiatorInList(playingField.gladiatorPlayer2, lineAttack, rowAttack) &&
            isGladiatorInList(playingField.gladiatorPlayer1, lineDest, rowDest))
            MoveType.ATTACK

        if (isGladiatorInList(playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2, lineDest, rowDest))
            MoveType.BLOCKED

        MoveType.LEGAL_MOVE
    }

    def isGladiatorInList (list: List[Gladiator], line: Int, row: Int): Boolean = {
        for (g <- list)
            if (g.row == row && g.line == line)
                true
        false
    }
}
