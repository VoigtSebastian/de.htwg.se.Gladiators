package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.util.Observable

class Controller extends Observable {
    val DIMENSIONS = 7
    var playingField: PlayingField = PlayingField(Array.ofDim[Cell](5, 5)).createRandom(DIMENSIONS)
    var gladiators = List(Gladiator(0, 0, 1.0, 1.0, 1.0, GladiatorType.TANK),
        Gladiator(0, 1, 1.0, 1.0, 1.0, GladiatorType.SWORD),
        Gladiator(0, 2, 1.0, 1.0, 1.0, GladiatorType.SWORD),
        Gladiator(1, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD),
        Gladiator(3, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD))



    def printHelpMessage(): String = {
        TuiEvaluator.generateHelpMessage()
    }

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
    }

    def createCommand(command:String): Vector[String] = {
        TuiEvaluator.evalCommand(command)
    }

    def printPlayingField(): String = {
        var str: String = ""
        /*
        for (i <- playingField.cells.indices)
            str = str + TuiEvaluator.evalPrintLine(playingField.formatLine(i)) + "\n"
        str*/
        playingField.toString()
    }
}
