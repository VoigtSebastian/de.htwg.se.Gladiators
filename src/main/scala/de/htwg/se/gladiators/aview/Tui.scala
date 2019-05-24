package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.{Controller, GladChanged, PlayingFieldChanged}
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.util.Observer

import scala.swing.Reactor

class Tui (controller: Controller) extends Reactor{

    //controller.add(this)
    listenTo(controller)

    def processInputLine(input: String): Unit = {
        val vec = controller.createCommand(input)
        val splitinput = input.split(",")
        vec(0) match {
            case "q" => ""
            case "n" => controller.createRandom();
            case "h" => showOutput(controller.printHelpMessage())
            case "t" => throw new NotImplementedError("toggle is not implemented yet")
            case "g" => controller.addGladiator(splitinput(1).toInt,splitinput(2).toInt,10,10,10,GladiatorType.SWORD); controller.printPlayingField()
            case "m" => controller.moveGladiator(splitinput(1).toInt, splitinput(2).toInt, splitinput(3).toInt,splitinput(4).toInt)
            case "u" => controller.undoGladiator()
            case _=> showOutput(controller.createCommand(input).toString())
            //case "g" =>
            /*
             case _ => {

               input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
                 case row :: column :: value :: Nil => grid.set(row, column, value)
                 case _ => grid
             */
        }
        controller.nextPlayer()

    }

    def showOutput(output: String): Unit = {
        println(output)
    }

    //override def update: Unit = print(controller.printPlayingField())

    reactions += {
        case event: PlayingFieldChanged => printPf()
        case event: GladChanged => printPf()
    }

    def printPf(): Unit = {
        print(controller.printPlayingField())
    }

}

