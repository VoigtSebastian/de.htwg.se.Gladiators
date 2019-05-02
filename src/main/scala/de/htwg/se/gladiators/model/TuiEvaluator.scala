package de.htwg.se.gladiators.model
import scala.util.matching.Regex

/**
  * Generates outputs which then can be shown on the Tui and evaluates commands
  */
object TuiEvaluator {
    //evalPrintLine formatters
    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")

    /**
      * Function that when entered a String that was formatted by an instance of PlayingField by using formatLine returns a colored PlayingField line ready to print out.
      * @param line returned by PlayingField.formatLine
      * @return a line which is colored and ready to be printed on Terminal.
      */
    def evalPrintLine(line: String): String = {
        var returnValue = ""
        for (c <- line) {
            c match {
                //gladiators
                case 'S' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.UNIT_BACKGROUND + " S " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> SWORD
                case 'B' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.UNIT_BACKGROUND + " B " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> BOW
                case 'M' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.UNIT_BACKGROUND + " M " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> MAGIC
                //cells
                case '0' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.SAND_BACKGROUND + " S " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> SAND
                case '1' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.PALM_BACKGROUND + " P " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> PALM
                case '2' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK + TuiEvaluator.BASE_BACKGROUND + " B " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> BASE
            }
        }
        returnValue
    }

    /**
      * Builds a help message to be printed on screen
      * @return a formatted help message that can be displayed to aid players on the Terminal version of Gladiators.
      */
    def generateHelpMessage():String = {
        "Gladiators instructions:\n-description-\n" +
            TEXT_COLOR_BLACK + ""  + SAND_BACKGROUND + " color of a sand tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + PALM_BACKGROUND + " color of a palm tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + BASE_BACKGROUND + " color of a base tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + UNIT_BACKGROUND +
            " color of a unit tile (S = Sword unit, B = Bow unit, M = Magic unit" +
            RESET_ANSI_ESCAPE + "\n\nYour current playingField!:"
    }

    /**
      * Returns a Vector of type String which includes all of the command blocks included in the function parameter.
      * @param input string that should contain some kind of command blocks
      * @return Vector that separated all of the commands blocks
      */
    def evalCommand(input: String): Vector[String] = {
        REGEX_COMMANDS.findAllIn(input).toVector
    }
}
