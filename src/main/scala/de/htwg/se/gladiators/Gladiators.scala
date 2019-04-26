package de.htwg.se.gladiators

import de.htwg.se.gladiators.model.{Cell, CellType, Gladiator, GladiatorType, PlayingField}
import de.htwg.se.gladiators.aview.Tui

object Gladiators {
    //evalPrintLine formatters
    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"
    //Other Strings
    val WAITING_FOR_INPUT = "=> "

    var tui = new Tui

    def main(args: Array[String]): Unit = {
        val cells = Array.ofDim[Cell](5, 5)
        var field = PlayingField(cells)

        var glad: List[Gladiator] = List(Gladiator(0, 0, 1.0, 1.0, 1.0, GladiatorType.MAGIC),
            Gladiator(0, 1, 1.0, 1.0, 1.0, GladiatorType.SWORD), Gladiator(0, 2, 1.0, 1.0, 1.0, GladiatorType.SWORD),
            Gladiator(1, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD), Gladiator(3, 3, 1.0, 1.0, 1.0, GladiatorType.SWORD))

        field = field.createRandom(5)

        var input: String = ""
        var output: String = ""
        do {
            print(WAITING_FOR_INPUT)
            input = scala.io.StdIn.readLine()// readLine()
            //(field, output)
            val tuple = tui.processInputLine(input, field)
            field = tuple._1
            output = tuple._2
            println(output)

            for (i <- field.cells.indices)
                evalPrintLine(field.formatLine(i, glad))
        } while (input != "q")
    }

    def evalPrintLine(line: String): Unit = {
        for (c <- line) {
            c match {
                //gladiators
                case 'S' => print(TEXT_COLOR_BLACK + UNIT_BACKGROUND + " S " + RESET_ANSI_ESCAPE) //-> SWORD
                case 'B' => print(TEXT_COLOR_BLACK + UNIT_BACKGROUND + " B " + RESET_ANSI_ESCAPE) //-> BOW
                case 'M' => print(TEXT_COLOR_BLACK + UNIT_BACKGROUND + " M " + RESET_ANSI_ESCAPE) //-> MAGIC
                //cells
                case '0' => print(TEXT_COLOR_BLACK + SAND_BACKGROUND + " S " + RESET_ANSI_ESCAPE) //-> SAND
                case '1' => print(TEXT_COLOR_BLACK + PALM_BACKGROUND + " P " + RESET_ANSI_ESCAPE) //-> PALM
                case '2' => print(TEXT_COLOR_BLACK + BASE_BACKGROUND + " B " + RESET_ANSI_ESCAPE) //-> BASE
            }
        }
        println()
    }

    def helpMessage(): String = {
        " "
    }
}
