package de.htwg.se.gladiators.aview
import scala.util.matching.Regex

import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.util.Observer


class Tui (controller: Controller) extends Observer with ShowMessage {
    controller.add(this)
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")

    def processInputLine(input: String): Unit = {
        val splitinput = evalCommand(input)//input.split(" ")
        splitinput(0) match {
            case "q" => showMessage("Exiting")
            case "n" => controller.createRandom()
            case "h" => showMessage(controller.printHelpMessage())
            case "t" => throw new NotImplementedError("toggle is not implemented yet")
            case "g" => controller.addGladiator(splitinput(1).toInt,splitinput(2).toInt,10,10,10,GladiatorType.SWORD); controller.printPlayingField()
            case "m" => controller.moveGladiator(splitinput(1).toInt, splitinput(2).toInt, splitinput(3).toInt,splitinput(4).toInt)
            case "u" => controller.undoGladiator()
            case _=> showMessage(splitinput.toString()) //showMessage(controller.createCommand(input).toString())
            //case "g" =>
            /*
             case _ => {

               input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
                 case row :: column :: value :: Nil => grid.set(row, column, value)
                 case _ => grid
             */
        }

    }

    override def showMessage(message: String): Unit = super.showMessage(message); println("")

    override def update: Unit = print(controller.printPlayingField())

    def evalCommand(input: String): Vector[String] = {
        REGEX_COMMANDS.findAllIn(input).toVector
    }

}

