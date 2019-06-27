package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.controllerComponent.{GladChanged, PlayingFieldChanged}

import scala.util.matching.Regex
import de.htwg.se.gladiators.controller.controllerComponent.PlayingFieldChanged
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.{Controller}
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.util.Observer

import scala.swing.Reactor

class Tui(controller: Controller) extends Reactor {

    //controller.add(this)
    listenTo(controller)
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")
    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"

    val CORRECT_FORMAT_MESSAGE = "Please use the correct format, press h to get help.\n"
    val RED_BOLD = "\033[1;31m"
    val LISTING: String = RED_BOLD + ">" + RESET_ANSI_ESCAPE
    val HELP_MESSAGE: String = "Gladiators instructions:" +
        "\nGladiators is a turn-based game which let's you buy, move and fight units on a chess-like playingField.\n" +
        TEXT_COLOR_BLACK + "" + SAND_BACKGROUND + " color of a sand tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + "" + PALM_BACKGROUND + " color of a palm tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + "" + BASE_BACKGROUND + " color of a base tile" + RESET_ANSI_ESCAPE + "\n" +
        TEXT_COLOR_BLACK + "" + UNIT_BACKGROUND +
        " color of a unit tile (S = Sword unit, B = Bow unit, T = Tank unit" +
        RESET_ANSI_ESCAPE + "\n" +
        LISTING + " Enter 'e' to end you turn\n" +
        LISTING + " Enter 's' to print the current shop with all the currently available gladiators you can buy\n" +
        LISTING + " Enter 'b', a shop index (press s to find out which one) and a coordinate to buy a unit and place it next to your base\n" +
        LISTING + " Enter 'm' with a pair of coordinates to move a unit from it's current coordinate to a new one\n" +
        LISTING + " Enter 'a' with a pair of coordinates to attack a unit\n" +
        LISTING + " Enter 't' to toggle the unit stats that are printed under the playingField as soon as you bought a unit\n" +
        LISTING + " Enter 'n' to generate a new playingField if you are unhappy with the current one\n" +
        LISTING + " Enter 'u' to undo a step\n" +
        LISTING + " Enter 'r' to redo a step\n" +
        LISTING + " Enter 'i' with a coordinate to show information about a unit (use t to toggle unit stats)\n"


    def processInputLine(input: String): Unit = {

        val splitinput = evalCommand(input)
        if (splitinput.isEmpty)
            return
        splitinput(0) match {
            case "q" => println("Exiting")
            case "n" => controller.createRandom(controller.playingField.size)
            case "h" => println(HELP_MESSAGE)
            case "t" => controller.toggleUnitStats()
            case "m" =>
                moveCommandBuilder(splitinput) match {
                    case Some(moveCommand) =>
                        println(controller.moveGladiator(moveCommand._1,
                            moveCommand._2,
                            moveCommand._3,
                            moveCommand._4)._2)
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }

            case "u" => controller.undoGladiator()
            case "r" => controller.redoGladiator()

            case "a" =>
                attackCommandBuilder(splitinput) match {
                    case Some(attackCommand) =>
                        println(controller.attack(attackCommand._1,
                            attackCommand._2,
                            attackCommand._3,
                            attackCommand._4)._2)
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "i" =>
                infoCommandBuilder(splitinput) match {
                    case Some(infoCommand) =>
                        println(controller.gladiatorInfo(infoCommand._1, infoCommand._2))
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "s" => println(controller.getShop)
            case "b" =>
                buyCommandBuilder(splitinput) match {
                    case Some(buyCommand) =>
                        println(controller.buyGladiator(buyCommand._1,
                            buyCommand._2,
                            buyCommand._3))
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "e" => println(controller.endTurn())

            case _ => println(splitinput.toString())
        }
    }

    reactions += {
        case event: PlayingFieldChanged => printPf()
        case event: GladChanged => printPf()
    }

    def buyCommandBuilder(v: Vector[String]): Option[(Int, Int, Int)] = {
        if (v.size != 4)
            None
        val ret = (toInt(v(1)), toInt(v(2)), toInt(v(3)))
        ret match {
            case (Some(i), Some(j), Some(k)) => Some(i, j, k)
            case _ => None
        }
    }

    def infoCommandBuilder(v: Vector[String]): Option[(Int, Int)] = {
        if (v.size != 3)
            None
        val ret = (toInt(v(1)), toInt(v(2)))
        ret match {
            case (Some(i), Some(j)) => Some(i, j)
            case _ => None
        }
    }

    def attackCommandBuilder(v: Vector[String]): Option[(Int, Int, Int, Int)] = {
        if (v.size != 5)
            None
        val ret = (toInt(v(1)), toInt(v(2)), toInt(v(3)), toInt(v(4)))
        ret match {
            case (Some(i), Some(j), Some(k), Some(l)) => Some(i, j, k, l)
            case _ => None
        }
    }

    def moveCommandBuilder(v: Vector[String]): Option[(Int, Int, Int, Int)] = {
        if (v.size != 5)
            None
        val ret = (toInt(v(1)), toInt(v(2)), toInt(v(3)), toInt(v(4)))
        ret match {
            case (Some(i), Some(j), Some(k), Some(l)) => Some(i, j, k, l)
            case _ => None
        }
    }

    def toInt(str: String): Option[Int] = {
        try {
            Some(str.toInt)
        } catch {
            case e: Exception => None
        }
    }

    def printPf(): Unit = {
        print(controller.printPlayingField())
    }

    def evalCommand(input: String): Vector[String] = {
        REGEX_COMMANDS.findAllIn(input).toVector
    }
}

