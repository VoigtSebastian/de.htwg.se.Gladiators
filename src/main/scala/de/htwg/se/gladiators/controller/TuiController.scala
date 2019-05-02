package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.aview.Tui

class TuiController extends ControllerInterface {
    val INPUT_BLUE = "\u001B[34m"
    val WAITING_FOR_INPUT:String = INPUT_BLUE + "▶ " + TuiEvaluator.RESET_ANSI_ESCAPE
    val DIMENSIONS = 7

    var tui = new Tui(this)
    var playingField: PlayingField = PlayingField(Array.ofDim[Cell](5, 5)).createRandom(DIMENSIONS)
    var gladiators = List(Gladiator(0, 0, 1.0, 1.0, 1.0, GladiatorType.MAGIC),
        Gladiator(0, 1, 1.0, 1.0, 1.0, GladiatorType.SWORD), Gladiator(0, 2, 1.0, 1.0, 1.0, GladiatorType.SWORD),
        Gladiator(1, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD), Gladiator(3, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD))

    def gameLoop (): Unit = {
        var input: String = ""
        var output: String = ""
        do {
            print(WAITING_FOR_INPUT)
            input = scala.io.StdIn.readLine()

            output = tui.processInputLine(input)

            tui.showOutput(output)

        } while (input != "q")
    }

    def printHelpMessage(): Unit = {
        tui.showOutput(TuiEvaluator.generateHelpMessage())
    }

    def createRandom(): Unit = {
        playingField = playingField.createRandom(DIMENSIONS)
    }

    def createCommand(command:String): Vector[String] = {
        TuiEvaluator.evalCommand(command)
    }

    def printPlayingField(): Unit = {
        for (i <- playingField.cells.indices)
            tui.showOutput(TuiEvaluator.evalPrintLine(playingField.formatLine(i, gladiators)))
    }
}
