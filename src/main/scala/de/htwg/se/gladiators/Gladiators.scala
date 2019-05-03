package de.htwg.se.gladiators
import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.{Cell, PlayingField}

object Gladiators {

    def main(args: Array[String]): Unit = {
        val RESET_ANSI_ESCAPE = "\033[0m"
        val INPUT_BLUE = "\u001B[34m"
        val WAITING_FOR_INPUT:String = INPUT_BLUE + "▶ " + RESET_ANSI_ESCAPE


        val controller = new Controller(new PlayingField(new Array[Array[Cell]]))
        val tui = new Tui(controller)

        var input: String = ""
        var output: String = ""
        do {
            print(WAITING_FOR_INPUT)
            input = scala.io.StdIn.readLine()

            output = tui.processInputLine(input)

            tui.showOutput(output)

        } while (input != "q")
    }
}
