package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command

case class Controller() extends ControllerInterface {
    override def inputCommand(command: Command): Unit = println(command)
}
