package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.BoardFactory
import de.htwg.se.gladiators.util.Factories.GladiatorFactory

class CoordinateSpec extends AnyWordSpec with Matchers {
    "A Board" when {
        "checking valid coordinates" should {
            "return all coordinates" in {
                val result = createSandBoard
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(TileType.Sand))

                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    result should contain(Coordinate(y, x))
                }
            }
            "return no coordinates" in {
                val result = createSandBoard
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(TileType.Palm))

                result should be(empty)
            }
            "return only walkable coordinates" in {
                val result = createNormalBoard
                    .getValidCoordinates(
                        Coordinate(1, 1),
                        6,
                        Vector(TileType.Sand))

                for (x <- (0 to 2)) {
                    result should contain(Coordinate(x, 1))
                }
                result should contain(Coordinate(0, 0))
                result should contain(Coordinate(2, 2))
            }
            "return only coordinates that can be attacked" in {
                val result = createNormalBoard
                    .getValidCoordinates(
                        Coordinate(1, 1),
                        6,
                        Vector(TileType.Sand, TileType.Base))

                for (x <- (0 to 2)) {
                    result should contain(Coordinate(x, 1))
                }

                result should contain(Coordinate(0, 0))
                result should contain(Coordinate(0, 1))
                result should contain(Coordinate(2, 1))
                result should contain(Coordinate(2, 2))
            }
        }
        "checking if coordinates are legal" should {
            val board = createSandBoard
            "return true" in {
                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    board.isCoordinateLegal(Coordinate(x, y)) should be(true)
                }
            }
            "return false" in {
                for ((x, y) <- (3 to 5).map(i => (i, i)) ++ (-3 to -1).map(i => (i, i))) {
                    board.isCoordinateLegal(Coordinate(x, y)) should be(false)
                }
            }
        }
        "being asked for a Tile" should {
            "return Sand" in {
                val board = createSandBoard
                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    board.tileAtCoordinate(Coordinate(0, 2)) should be(TileType.Sand)
                }
            }
        }
        "working on a string representation" should {
            val board = BoardFactory.initRandomBoard()
            "return None" in {
                board.gladiatorAtPosition(Vector(), Coordinate(1, 1)) should be(None)
            }
            "return Some" in {
                val gladiator = GladiatorFactory.createGladiator(position = Some(Coordinate(0, 0)))
                board.gladiatorAtPosition(Vector(gladiator), Coordinate(0, 0)) should be(Some(gladiator.gladiatorType))
            }
            "return a string" in {
                board.coloredString(Vector()).isInstanceOf[String] should be(true)
            }
        }
    }

    def createSandBoard: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand))
        Board(tiles)
    }

    def createNormalBoard: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Base, TileType.Palm),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Palm, TileType.Base, TileType.Sand))
        Board(tiles)
    }
}
