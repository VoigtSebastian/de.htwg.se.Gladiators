package de.htwg.se.gladiators.aview

import kaleidoscope._
import scala.swing.Reactor

import de.htwg.se.gladiators.util.Events.{ Init, PlayerOneNamed, PlayerTwoNamed, Turn }
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
        case Turn(player) => println(s"It's ${player.name}s turn!\n${controller.boardToString}")
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
            }
            case r"(end|e)" => controller.inputCommand(EndTurn)
            case _ => couldNotParseCommand(line)
        }
        true
    }

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
