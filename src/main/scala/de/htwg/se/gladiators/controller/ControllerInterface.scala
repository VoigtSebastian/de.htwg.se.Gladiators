package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command
import scala.swing.Publisher
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Coordinate

trait ControllerInterface extends Publisher {
    var gameState: GameState = GameState.NamingPlayerOne
    def inputCommand(command: Command): Events

    def namePlayerOne(name: String): Events
    def namePlayerTwo(name: String): Events
    def endTurn: Events
    def buyUnit(number: Int, position: Coordinate): Events
    def move(from: Coordinate, to: Coordinate): Events

    def tileOccupiedByCurrentPlayer(tile: Coordinate): Boolean
    def attackTiles(tile: Coordinate): Option[Vector[Coordinate]]
    def moveTiles(tile: Coordinate): Option[Vector[Coordinate]]
    def newUnitPlacementTiles: Option[Vector[Coordinate]]

    def boardToString: String
    def shopToString: String
}
