package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.model.GladiatorType._
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.MovementType._

class MovesSpec extends AnyWordSpec with Matchers {
    "A Move" when {
        "categorizing moving units" should {
            val enemyPlayer = Player("", 2, Vector())
            val board = createNormalBoard
            "categorize legal moves" in {
                val currentPlayer = Player("", 0, Vector(Gladiator(Knight, Coordinate(0, 0), 100, 4)))

                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((2, 2))) {
                    Moves.movementType(Coordinate(0, 0), Coordinate(x, y), board, currentPlayer, enemyPlayer) should be(Move)
                }
            }
            "categorize illegal moves" in {
                val currentPlayer = Player("", 0, Vector(Gladiator(Knight, Coordinate(0, 0), 100, 0)))

                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((2, 2))) {
                    Moves.movementType(Coordinate(0, 0), Coordinate(x, y), board, currentPlayer, enemyPlayer) should be(IllegalMove)
                }
            }
            "categorize a tile as blocked" in {
                val currentPlayer = Player("", 0, Vector(Gladiator(Knight, Coordinate(0, 0), 100, 1), Gladiator(Knight, Coordinate(0, 1), 100, 1)))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 0), board, currentPlayer, enemyPlayer) should be(TileBlocked)
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 1), board, currentPlayer, enemyPlayer) should be(TileBlocked)

            }
            "categorize a unit as not owned by this Player" in {
                val currentPlayer = Player("", 0, Vector(Gladiator(Knight, Coordinate(0, 0), 100, 0)))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 0), board, enemyPlayer, currentPlayer) should be(NotOwnedByPlayer)
            }
            "categorize a free tile as no unit at this Tile" in {
                val currentPlayer = Player("", 0, Vector())
                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((0, 0))) {
                    Moves.movementType(Coordinate(x, y), Coordinate(0, 0), board, currentPlayer, enemyPlayer) should be(NoUnitAtCoordinate)
                }
            }
        }
    }

    def createNormalBoard: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Base, TileType.Palm),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Palm, TileType.Base, TileType.Sand))
        Board(tiles)
    }
}
