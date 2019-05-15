package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.GameStatus._
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.util.Observable
import de.htwg.se.gladiators.util.UndoManager

import scala.collection.generic.IdleSignalling

class Controller(var playingField : PlayingField) extends Observable {
    private val undoManager = new UndoManager
    val DIMENSIONS = 7

    var gameStatus: GameStatus = IDLE

    def printHelpMessage(): String = {
        TuiEvaluator.generateHelpMessage()
    }

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
        notifyObservers
    }

    def createCommand(command:String): Vector[String] = {
        //notifyObservers
        TuiEvaluator.evalCommand(command)
    }

    def printPlayingField(): String = {
        var str: String = ""
        /*
        for (i <- playingField.cells.indices)
            str = str + TuiEvaluator.evalPrintLine(playingField.formatLine(i)) + "\n"
        str*/
       // notifyObservers
        playingField.toString()
    }
    def addGladiator(line: Int, row: Int, movementPoints: Double, ap: Double, hp: Double, gladiatorType: GladiatorType): Unit = {

        var glad = Gladiator(line, row, movementPoints, ap, hp, gladiatorType)
        playingField = playingField.createGladiator(glad)
        notifyObservers
        playingField
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): Unit = {
        //playingField = playingField.moveGladiator(x,y,xDest,yDest)
        undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
        notifyObservers
    }
}
