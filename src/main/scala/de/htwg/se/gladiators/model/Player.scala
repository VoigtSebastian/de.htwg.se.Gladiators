package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.model.TileType.Sand

case class Player(
    name: String,
    enemyBaseLine: Int,
    credits: Int,
    gladiators: Vector[Gladiator] = Vector()) {

    def placementTilesNewUnit(boardSize: Int, tiles: Vector[Vector[TileType]]): Vector[Coordinate] = {
        val (nextToBase, aboveBase) = enemyBaseLine match {
            case 0 => (boardSize - 1, boardSize - 2)
            case _ => (0, 1)
        }
        Vector(
            Coordinate((boardSize / 2 - 1), nextToBase),
            Coordinate((boardSize / 2 + 1), nextToBase),
            Coordinate(boardSize / 2, aboveBase)).filter(coordinate => tiles(coordinate.y)(coordinate.x) == Sand && gladiators.filter(gladiator => gladiator.position == coordinate).isEmpty)
    }
}
