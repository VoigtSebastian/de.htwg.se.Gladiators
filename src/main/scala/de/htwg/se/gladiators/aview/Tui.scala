package de.htwg.se.gladiators.aview

import kaleidoscope._
import scala.swing.Reactor

import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.controller.GameState

case class Tui(controller: ControllerInterface) extends Reactor {
    listenTo(controller)
    val namePlayerOneMessage = "Name Player One: "
    val namePlayerTwoMessage = "Name Player Two: "

    reactions += {
        case Init => print(namePlayerOneMessage)
        case PlayerOneNamed(_) => print(namePlayerTwoMessage)
        case PlayerTwoNamed(_) => println("Game start")
        case Turn(player) => {
            println(s"It's ${player.name}s turn!")
            printBoard
        }
        case SuccessfullyBoughtGladiator(player, gladiator) => {
            printBoard
            println(f"${player.name} successfully bought the gladiator at position ${gladiator.position}")
        }
        case Moved(player, from, to, _) => {
            printBoard
            println(f"${player.name} moved a unit from ${from} to ${to}")
        }
        case Attacked(player, killed, _, position) => println(f"${player.name} attacked ${if (killed) "and killed"} an unit at position $position!")
        case ErrorMessage(message) => errorMessage(message)
        case Shutdown => println("Goodbye!")
        case Won(player) => println(f"Congratulations, ${player.name} won!")
    }

    def processInputLine(line: String): Boolean = {
        line.toLowerCase match {
            case r"quit( .*)?" => {
                controller.inputCommand(Quit)
                return false
            }
            case r".*" if controller.gameState == GameState.NamingPlayerOne => {
                controller.inputCommand(NamePlayerOne(line.replace(namePlayerOneMessage, "")))
            }
            case r".*" if controller.gameState == GameState.NamingPlayerTwo => {
                controller.inputCommand(NamePlayerTwo(line.replace(namePlayerTwoMessage, "")))
            }
            case r"move \d+ \d+ \d+ \d+" => {
                val args = commandBuilder(line)
                controller.inputCommand(
                    Move(
                        Coordinate(args(0), args(1)),
                        Coordinate(args(2), args(3))))
            }
            case r"(buy|b) \d+ \d+ \d+" => {
                val args = commandBuilder(line)
                controller.inputCommand(
                    BuyUnit(
                        args(0),
                        Coordinate(args(1), args(2))))
            }
            case r"(end|e)" => controller.inputCommand(EndTurn)
            case r"(shop|s)" => println(controller.shopToString)
            case _ => couldNotParseCommand(line)
        }
        true
    }

    def printBoard = println(controller.boardToString)
    def errorMessage(line: String) = println(s"Error: ($line)")
    def couldNotParseCommand(line: String) = errorMessage(f"Could not parse command: ($line)")

    def commandBuilder(line: String): List[Int] =
        line
            .toLowerCase
            .split("[ |,|;|.]")
            .drop(1)
            .map(number => number.toInt)
            .toList
}
