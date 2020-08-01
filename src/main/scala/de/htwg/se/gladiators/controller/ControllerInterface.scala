package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command
import scala.swing.Publisher

trait ControllerInterface extends Publisher {
    var gameState: GameState = GameState.NamingPlayerOne
    def inputCommand(command: Command): Unit
}
