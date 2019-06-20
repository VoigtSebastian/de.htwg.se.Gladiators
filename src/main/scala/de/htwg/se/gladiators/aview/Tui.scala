package de.htwg.se.gladiators.aview

import scala.util.matching.Regex
import de.htwg.se.gladiators.controller.{Controller, GameStatus, GladChanged, PlayingFieldChanged}
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.util.Observer

import scala.swing.Reactor

class Tui (controller: Controller) extends Reactor with ShowMessage {

    //controller.add(this)
    listenTo(controller)
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")
    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"

    val WRONG_FORMAT: String = "Please enter the create command in the correct format, press h to get help"
    val HELP_MESSAGE: String = "Gladiators instructions:\n-description-\n" +
        TEXT_COLOR_BLACK + ""  + SAND_BACKGROUND + " color of a sand tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + ""  + PALM_BACKGROUND + " color of a palm tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + ""  + BASE_BACKGROUND + " color of a base tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + ""  + UNIT_BACKGROUND +
        " color of a unit tile (S = Sword unit, B = Bow unit, T = Tank unit" +
        RESET_ANSI_ESCAPE + "\n\nYour current playingField!"


    def processInputLine(input: String): Unit = {

        val splitinput = evalCommand(input)//input.split(" ")
        splitinput(0) match {
            case "q" => showMessage("Exiting")
            case "n" => controller.createRandom(controller.playingField.size)
            case "h" => println(HELP_MESSAGE)
            case "t" => controller.toggleUnitStats()
            case "g" =>
                if (splitinput.size == 3) {
                    controller.addGladiator(splitinput(1).toInt, splitinput(2).toInt)
                    controller.printPlayingField()
                }
                else
                    println(WRONG_FORMAT)

            case "m" =>
                if (splitinput.size == 5)
                    println(controller.moveGladiator(splitinput(1).toInt, splitinput(2).toInt, splitinput(3).toInt,splitinput(4).toInt))
                else
                    println(WRONG_FORMAT)

            case "u" => controller.undoGladiator()
            case "r" => controller.redoGladiator()

            case "a" =>
                if (splitinput.size == 5)
                    println(controller.attack(splitinput(1).toInt, splitinput(2).toInt, splitinput(3).toInt, splitinput(4).toInt))
                else
                    println(WRONG_FORMAT)

            case "i" =>
                if (splitinput.size == 3)
                    println(controller.gladiatorInfo(splitinput(1).toInt, splitinput(2).toInt))
                else
                    println(WRONG_FORMAT)
            case "s" => println(controller.getShop)
            case "b" =>
                if (splitinput.size == 2)
                    println(controller.buyGladiator(splitinput(1).toInt))
                    //TODO: Position for glad
                else
                    println(WRONG_FORMAT)
            case _=> showMessage(splitinput.toString()) //showMessage(controller.createCommand(input).toString())
        }
        //controller.nextPlayer()
    }

    override def showMessage(message: String): Unit = super.showMessage(message); println("")

    //override def update: Unit = print(controller.printPlayingField())

    reactions += {
        case event: PlayingFieldChanged => printPf()
        case event: GladChanged => printPf()
    }

    def printPf(): Unit = {
        print(controller.printPlayingField())
    }

    def evalCommand(input: String): Vector[String] = {
        REGEX_COMMANDS.findAllIn(input).toVector
    }
}

