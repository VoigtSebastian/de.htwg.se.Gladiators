package de.htwg.se.gladiators.controller

import scala.swing.Publisher

import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.model.TileType
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Events

trait ControllerInterface extends Publisher {
    def gameState: GameState
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
    def boardTiles: Vector[Vector[TileType]]

    def gladiatorsPlayerOne: Option[Vector[Gladiator]]
    def gladiatorsPlayerTwo: Option[Vector[Gladiator]]

    def stock: Vector[Gladiator]

    def boardToColoredString: String
    def boardToString: String
    def shopToString: String
}
