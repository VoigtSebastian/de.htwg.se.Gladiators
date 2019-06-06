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
        playingField = playingField.createGladiator(GladiatorFactory.createGladiator(line, row, gladiatorType), gameStatus)
        players(gameStatus.id).buyItem(10)
        //notifyObservers
        publish(new GladChanged)
        //playingField
    }

    def gladiatorInfo (line: Int, row: Int): String = {
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
        playingField.attack(lineAttack, rowAttack, lineDest, rowDest, gameStatus)
        if (gameStatus == P1) {

        } else if (gameStatus == P2) {

        }
        ""
    }
}
