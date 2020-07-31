package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command

trait ControllerInterface {
    var gameState = GameState.NamingPlayerOne
    def inputCommand(command: Command): Unit
}
