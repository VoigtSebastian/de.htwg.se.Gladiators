package de.htwg.se.gladiators.aview

import de.htwg.se.gladiators.controller.controllerComponent.{ControllerInterface, GladChanged, PlayingFieldChanged}
import scala.util.matching.Regex
import scala.swing.Reactor
import scala.util.{Try,Success,Failure}

class Tui (controller: ControllerInterface) extends Reactor {

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

        val splitInput = evalCommand(input)
        if (splitInput.isEmpty)
            return
        splitInput(0) match {
            case "q" => println("Exiting")
            case "n" => controller.createRandom(controller.playingField.size)
            case "h" => println(HELP_MESSAGE)
            case "t" => controller.toggleUnitStats()
            case "m" =>
                commandBuilder(splitInput.patch(0, Nil, 1)) match {
                    case Some(moveCommand) =>
                        println(controller.moveGladiator(moveCommand(0),
                            moveCommand(1),
                            moveCommand(2),
                            moveCommand(3))._2)
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }

            case "u" => controller.undoGladiator()
            case "r" => controller.redoGladiator()

            case "a" =>
                commandBuilder(splitInput.patch(0, Nil, 1)) match {
                    case Some(attackCommand) =>
                        println(controller.attack(attackCommand(0),
                            attackCommand(1),
                            attackCommand(2),
                            attackCommand(3))._2)
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "i" =>
                commandBuilder(splitInput.patch(0, Nil, 1)) match {
                    case Some(infoCommand) =>
                        println(controller.gladiatorInfo(infoCommand(0), infoCommand(1)))
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "s" => println(controller.getShop)
            case "b" =>
                commandBuilder(splitInput.patch(0, Nil, 1)) match {
                    case Some(buyCommand) =>
                        println(controller.buyGladiator(buyCommand(0),
                            buyCommand(1),
                            buyCommand(2)))
                    case None => println(CORRECT_FORMAT_MESSAGE)
                }
            case "e" => println(controller.endTurn())

            case _ => println(splitInput.toString())
        }
    }

    reactions += {
        case event: PlayingFieldChanged => printPf()
        case event: GladChanged => printPf()
    }

    def toInt(s: String): Try[Int] = Try(Integer.parseInt(s.trim))

    def commandBuilder(v: Vector[String]): Option[Vector[Int]] = {
        val ret: Vector[Int] = v.map(s => {
            toInt(s) match {
                case Success(i) => i
                case _ => return None
            }
        })
        Some(ret)
    }

    def printPf(): Unit = {
        print(controller.printPlayingField())
    }

    def evalCommand(input: String): Vector[String] = {
        REGEX_COMMANDS.findAllIn(input).toVector
    }
}

