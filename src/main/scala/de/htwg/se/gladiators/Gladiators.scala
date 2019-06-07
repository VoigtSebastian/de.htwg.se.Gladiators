package de.htwg.se.gladiators
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.aview.gui.SwingGui
import de.htwg.se.gladiators.controller.{Controller, PlayingFieldChanged}
import de.htwg.se.gladiators.model.{Cell, PlayingField}

object Gladiators {

    def main(args: Array[String]): Unit = {
        val RESET_ANSI_ESCAPE = "\033[0m"
        val INPUT_BLUE = "\u001B[34m"
        val WAITING_FOR_INPUT:String = INPUT_BLUE + "▶ " + RESET_ANSI_ESCAPE

        val controller = new Controller(PlayingField())
       // controller.createRandom()
        val tui = new Tui(controller)
        val gui = new SwingGui(controller)
        //controller.notifyObservers
        controller.publish(new PlayingFieldChanged)
        var input: String  = ""
        if(args.length > 0) input = args(0)
        var output: String = ""
        if (!input.isEmpty) tui.processInputLine(input)
        else do {
            println(WAITING_FOR_INPUT)
            input = scala.io.StdIn.readLine()
            tui.processInputLine(input)
        } while (input != "q")
    }
}
