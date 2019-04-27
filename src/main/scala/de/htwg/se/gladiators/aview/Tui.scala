package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.model.{Cell, Gladiator, Player, PlayingField}


class Tui {

    //evalPrintLine formatters
    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"

    def generateHelpMessage(): String = {
        val ret: String = "Gladiators instructions:\n-description-\n" +
            TEXT_COLOR_BLACK + ""  + SAND_BACKGROUND + " color of a sand tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + PALM_BACKGROUND + " color of a palm tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + BASE_BACKGROUND + " color of a base tile" + RESET_ANSI_ESCAPE + "\n" +
            TEXT_COLOR_BLACK + ""  + UNIT_BACKGROUND +
            " color of a unit tile (S = Sword unit, B = Bow unit, M = Magic unit" +
            RESET_ANSI_ESCAPE + "\n\nYour current playingField!:"
        ret
    }

    def processInputLine(input: String, pf: PlayingField): (PlayingField, String) = {

        input match {
            case "q" => (pf, "")
            case "n" => (pf.createRandom(11), "New playingFiled created: ")
            case "h" => (pf, generateHelpMessage())
            case "t" => (pf, "implement toggle unit color?")
            case _=> (pf, generateHelpMessage()) //TODO: Make units addressable
            //case "g" =>
            /*
             case _ => {

               input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
                 case row :: column :: value :: Nil => grid.set(row, column, value)
                 case _ => grid
             */
        }

    }


}

