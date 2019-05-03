package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.Controller


class Tui (controller: Controller){
    def processInputLine(input: String): String = {
        input match {
            case "q" => ""
            case "n" => controller.createRandom(); controller.printPlayingField()
            case "h" => controller.printHelpMessage()
            case "t" => "implement toggle unit color?"
            case _=> controller.createCommand(input).toString()
            //case "g" =>
            /*
             case _ => {

               input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
                 case row :: column :: value :: Nil => grid.set(row, column, value)
                 case _ => grid
             */
        }

    }

    def showOutput(output: String): Unit = {
        println(output)
    }


}

