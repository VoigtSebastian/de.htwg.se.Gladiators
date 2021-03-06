package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.TileType.Mine
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.GladiatorFactory.createGladiator
import de.htwg.se.gladiators.util.MovementType._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MovesSpec extends AnyWordSpec with Matchers {
    "A Move" when {
        "categorizing moving units" should {
            val enemyPlayer = Player(2, "", 2, 0, 100, false, Vector())
            val board = createNormalBoard
            "categorize legal moves" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(4), moved = Some(false))))

                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((2, 2))) {
                    Moves.movementType(Coordinate(0, 0), Coordinate(x, y), board, currentPlayer, enemyPlayer) should be(Move)
                }
            }
            "categorize illegal moves" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(0), moved = Some(false))))

                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((2, 2))) {
                    Moves.movementType(Coordinate(0, 0), Coordinate(x, y), board, currentPlayer, enemyPlayer) should be(IllegalMove)
                }
            }
            "categorize a tile as blocked" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(1), moved = Some(false)), createGladiator(position = Some(Coordinate(0, 1)))))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 0), board, currentPlayer, enemyPlayer) should be(TileBlocked)
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 1), board, currentPlayer, enemyPlayer) should be(TileBlocked)

            }
            "categorize a unit as not owned by this Player" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(0), moved = Some(false))))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 0), board, enemyPlayer, currentPlayer) should be(NotOwnedByPlayer)
            }
            "categorize a free tile as no unit at this Tile" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector())
                for ((x, y) <- (0 to 2).map((_, 1)) ++ Vector((0, 0))) {
                    Moves.movementType(Coordinate(x, y), Coordinate(0, 0), board, currentPlayer, enemyPlayer) should be(NoUnitAtCoordinate)
                }
            }
            "categorize a move to palm" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 1)), moved = Some(false))))
                Moves.movementType(Coordinate(0, 1), Coordinate(0, 2), board, currentPlayer, enemyPlayer) should be(MoveToPalm)
            }
            "categorize an illegal move when moving onto the own Base" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(2, 2)), movementPoints = Some(1), moved = Some(false))))
                Moves.movementType(Coordinate(2, 2), Coordinate(1, 2), board, currentPlayer, enemyPlayer) should be(IllegalMove)
            }
            "categorize a base-attack" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), moved = Some(false))))
                Moves.movementType(Coordinate(0, 0), Coordinate(1, 0), board, currentPlayer, enemyPlayer) should be(BaseAttack)
            }
            "categorize that the Gladiator already moved" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(2))))
                Moves.movementType(Coordinate(0, 0), Coordinate(1, 0), board, currentPlayer, enemyPlayer) should be(AlreadyMoved)
            }
            "categorize mining" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(2), moved = Some(false))))
                val goldBoard = board.copy(tiles = board.tiles.updated(1, board.tiles(0).updated(0, Mine(500))))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 1), goldBoard, currentPlayer, enemyPlayer) should be(Mining)
            }
            "categorize an illegal move, because the mine  is to far away" in {
                val currentPlayer = Player(1, "", 0, 0, 100, false, Vector(createGladiator(position = Some(Coordinate(0, 0)), movementPoints = Some(0), moved = Some(false))))
                val goldBoard = board.copy(tiles = board.tiles.updated(1, board.tiles(0).updated(0, Mine(500))))
                Moves.movementType(Coordinate(0, 0), Coordinate(0, 1), goldBoard, currentPlayer, enemyPlayer) should be(IllegalMove)
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
