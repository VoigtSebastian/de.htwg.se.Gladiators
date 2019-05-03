package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.util.Observer


class Tui (controller: Controller) extends Observer{

    controller.add(this)

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

    override def update: Unit = print(controller.printPlayingField())

}

