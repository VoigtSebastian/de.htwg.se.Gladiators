package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.GameStatus._
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.util.Observable
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

class Controller(var playingField : PlayingField) extends Publisher {

    private val undoManager = new UndoManager
    val DIMENSIONS = 7
    var gameStatus: GameStatus = P1
    var players = Array(Player("Player1"), Player("Player2"))

    def printHelpMessage(): String = {
        TuiEvaluator.generateHelpMessage()
    }

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
        //notifyObservers
        publish(new PlayingFieldChanged)
    }

    def createCommand(command:String): Vector[String] = {
        //notifyObservers
        TuiEvaluator.evalCommand(command)
    }

    def printPlayingField(): String = {
        // notifyObservers
        playingField.toString + players(gameStatus.id) + "\n"
    }
    def addGladiator(line: Int, row: Int, movementPoints: Double, ap: Double, hp: Double, gladiatorType: GladiatorType): Unit = {

        var glad = Gladiator(line, row, movementPoints, ap, hp, gladiatorType, players(gameStatus.id))
        players(gameStatus.id).buyItem(10)
        playingField = playingField.createGladiator(glad)
        //notifyObservers
        publish(new GladChanged)
        playingField
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): Unit = {
        //playingField = playingField.moveGladiator(x,y,xDest,yDest)
        undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
        //notifyObservers
        publish(new GladChanged)
    }

    def undoGladiator(): Unit = {
        undoManager.undoStep
    }

    def nextPlayer(): Unit = {
        if (gameStatus == P1) {
            gameStatus = P2
        } else {
            gameStatus = P1
        }
    }

    def cell(line: Int, row: Int) = playingField.cell(line, row)

}
