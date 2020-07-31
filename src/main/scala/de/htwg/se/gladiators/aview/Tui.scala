package de.htwg.se.gladiators.aview

import kaleidoscope._
import scala.util.{ Try, Failure, Success }
import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Coordinate

case class Tui(controller: ControllerInterface) {

    def processInputLine(line: String): Boolean = {
        line.toLowerCase match {
            case r"quit( .*)?" => {
                controller.inputCommand(Quit)
                false
            }
            case r"move \d+ \d+ \d+ \d+" => {
                val args = commandBuilder(line)
                controller.inputCommand(
                    Move(
                        Coordinate(args(0), args(1)),
                        Coordinate(args(2), args(3))))
                true
            }
            case _ => {
                errorMessage(line)
                true
            }
        }
    }

    def errorMessage(line: String) = s"Could not parse command: ($line)"

    def commandBuilder(line: String): List[Int] =
        line
            .toLowerCase
            .split("[ |,|;|.]")
            .drop(1)
            .map(number => number.toInt)
            .toList
}
