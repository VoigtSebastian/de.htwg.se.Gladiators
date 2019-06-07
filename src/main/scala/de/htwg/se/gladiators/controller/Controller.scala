package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.GameStatus._
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.util.Observable
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

class Controller(var playingField: PlayingField) extends Publisher {

    private val undoManager = new UndoManager
    val DIMENSIONS = 7
    var gameStatus: GameStatus = P1
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
        if (isCoordinateLegal(lineDest, rowDest))
            undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
        publish(new GladChanged)
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

    def cell(line: Int, row: Int) = playingField.cell(line, row)


    def redoGladiator(): Unit = {
        undoManager.redoStep
    }

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): String = {
        if (isCoordinateLegal(lineAttack, rowAttack) && isCoordinateLegal(lineDest, rowDest)) {
            var gladA = GladiatorFactory.createGladiator(Int.MinValue, Int.MinValue, GladiatorType.SWORD, players(P1.id))
            var gladB = GladiatorFactory.createGladiator(Int.MinValue, Int.MinValue, GladiatorType.SWORD, players(P2.id))
            if (gameStatus == P1) {
                println("Player one should attack")
                val gladAttack = attackFindGladiator(playingField.gladiatorPlayer1, lineAttack, rowAttack, gladA)
                val gladDest = attackFindGladiator(playingField.gladiatorPlayer2, lineDest, rowDest, gladB)
                if (gladAttack._2 && gladDest._2)
                    playingField.attack(gladAttack._1, gladDest._1)
                else
                    "There was no gladiator at this position"
            } else if (gameStatus == P2) {
                println("Player two should attack")
                val gladAttack = attackFindGladiator(playingField.gladiatorPlayer2, lineAttack, rowAttack, gladA)
                val gladDest = attackFindGladiator(playingField.gladiatorPlayer1, lineDest, rowDest, gladB)
                if (gladAttack._2 && gladDest._2)
                    playingField.attack(gladAttack._1, gladDest._1)
                else
                    "Please enter correct coordinates"
            } else
                "It is not possible to attack in this state"
        }
        else
            "Coordinates are out of bounds!"
    }

    def attackFindGladiator (gladiatorList: List[Gladiator], line: Int, row: Int, gladiatorDestination: Gladiator): (Gladiator, Boolean) = {
        for (g <- gladiatorList) {
            if (g.line == line && g.row == row)
                return (g, true)
        }
        (gladiatorDestination, false)
    }

    def cellSelected(line: Int, row: Int): Unit = {
        selectedCell = (line, row)
        publish(new CellClicked)
    }
}
