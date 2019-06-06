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
      * Function that when entered a String that was formatted by an instance of
      * PlayingField by using formatLine returns a colored PlayingField line ready to print out.
      * @param line returned by PlayingField.formatLine
      * @return a line which is colored and ready to be printed on Terminal.
      */
    def evalPrintLine(line: String): String = {
        var returnValue = ""
        for (c <- line) {
            c match {
                //gladiators
                case 'S' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.UNIT_BACKGROUND + " S " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> SWORD
                case 'B' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.UNIT_BACKGROUND + " B " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> BOW
                case 'T' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.UNIT_BACKGROUND + " T " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> TANK
                //cells
                case '0' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.SAND_BACKGROUND + " S " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> SAND
                case '1' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.PALM_BACKGROUND + " P " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> PALM
                case '2' => returnValue = returnValue + (TuiEvaluator.TEXT_COLOR_BLACK +
                    TuiEvaluator.BASE_BACKGROUND + " B " + TuiEvaluator.RESET_ANSI_ESCAPE) //-> BASE
            }
        }
        returnValue
    }
}
