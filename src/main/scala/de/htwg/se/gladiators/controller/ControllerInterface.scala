package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command

trait ControllerInterface {
    def inputCommand(command: Command): Unit
}
