package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.util.Observable

class Controller(var playingField : PlayingField) extends Observable {
    val DIMENSIONS = 7

    def printHelpMessage(): String = {
        TuiEvaluator.generateHelpMessage()
    }

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
        notifyObservers
    }

    def createCommand(command:String): Vector[String] = {
        TuiEvaluator.evalCommand(command)
        notifyObservers
    }

    def printPlayingField(): String = {
        var str: String = ""
        /*
        for (i <- playingField.cells.indices)
            str = str + TuiEvaluator.evalPrintLine(playingField.formatLine(i)) + "\n"
        str*/
        playingField.toString()
        notifyObservers
    }
    def addGladiator(x: Int, y: Int, movementPoints: Double, ap: Double, hp: Double, gladiatorType: GladiatorType): Unit = {
        var glad = new Gladiator(x, y, movementPoints, ap, hp, gladiatorType)
        playingField = playingField.createGladiator(glad)
        notifyObservers
    }
}
