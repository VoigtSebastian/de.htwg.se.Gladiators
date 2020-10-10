package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command
import scala.swing.Publisher
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Coordinate

trait ControllerInterface extends Publisher {
    var gameState: GameState = GameState.NamingPlayerOne
    def inputCommand(command: Command): Events
    def boardToString: String
    def shopToString: String
    def namePlayerOne(name: String): Events
    def namePlayerTwo(name: String): Events
    def endTurn: Events
    def buyUnit(number: Int, position: Coordinate): Events
    def move(from: Coordinate, to: Coordinate): Events
}
