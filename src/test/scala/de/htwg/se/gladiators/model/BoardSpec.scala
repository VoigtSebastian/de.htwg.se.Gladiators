package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.TileType._
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.BoardFactory
import de.htwg.se.gladiators.util.Factories.GladiatorFactory

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import BoardFactory.{ createNormalBoard3x3, createSandBoard3x3 }

class CoordinateSpec extends AnyWordSpec with Matchers {
    "A Board" when {
        "updating tiles" should {
            "update to Palm" in {
                createSandBoard3x3
                    .updateTile(Coordinate(0, 0), Palm)
                    .tiles(0)(0) should be(Palm)

                createSandBoard3x3
                    .updateTile(Coordinate(1, 2), Palm)
                    .tiles(2)(1) should be(Palm)
            }
            "update to Base" in {
                val board = createSandBoard3x3
                (0 to 2).zip(0 to 2).foreach {
                    case (x, y) => board
                        .updateTile(Coordinate(x, y), Base)
                        .tiles(y)(x) should be(Base)
                }
            }
        }
        "checking valid coordinates" should {
            "return all coordinates" in {
                val result = createSandBoard3x3
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(TileType.Sand))

                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    result should contain(Coordinate(y, x))
                }
            }
            "return no coordinates" in {
                val result = createSandBoard3x3
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(TileType.Palm))

                result should be(empty)
            }
            "return only walkable coordinates" in {
                val result = createNormalBoard3x3
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
                val result = createNormalBoard3x3
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
            val board = createSandBoard3x3
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
                val board = createSandBoard3x3
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
}
