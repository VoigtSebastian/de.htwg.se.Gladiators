package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.util.Observer


class Tui (controller: Controller) extends Observer{

    controller.add(this)

    def processInputLine(input: String): Unit = {
        val splitinput = input.split(",")
        splitinput(0) match {
            case "q" => ""
            case "n" => controller.createRandom();
            case "h" => showOutput(controller.printHelpMessage())
            case "t" => throw new NotImplementedError("toggle is not implemented yet")
            case "g" => controller.addGladiator(splitinput(1).toInt,splitinput(2).toInt,10,10,10,GladiatorType.SWORD); controller.printPlayingField()
            case _=> showOutput(controller.createCommand(input).toString())
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

