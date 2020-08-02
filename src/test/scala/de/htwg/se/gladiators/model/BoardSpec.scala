package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.gladiators.util.Coordinate

class CoordinateSpec extends AnyWordSpec with Matchers {
    "A Board" when {
        "checking valid coordinates" should {
            "return all coordinates" in {
                val result = createSandBoard
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(CellType.Sand))

                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    result should contain(Coordinate(y, x))
                }
            }
            "return no coordinates" in {
                val result = createSandBoard
                    .getValidCoordinates(
                        Coordinate(0, 0),
                        6,
                        Vector(CellType.Palm))

                result should be(empty)
            }
            "return only walkable coordinates" in {
                val result = createNormalBoard
                    .getValidCoordinates(
                        Coordinate(1, 1),
                        6,
                        Vector(CellType.Sand))

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
                        Vector(CellType.Sand, CellType.Base))

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
        "being asked for a Cell" should {
            "return Sand" in {
                val board = createSandBoard
                for ((x, y) <- (0 to 2).map(i => (i, i))) {
                    board.cellAtCoordinate(Coordinate(0, 2)) should be(CellType.Sand)
                }
            }
        }
    }

    def createSandBoard: Board = {
        val cells = Vector(
            Vector(CellType.Sand, CellType.Sand, CellType.Sand),
            Vector(CellType.Sand, CellType.Sand, CellType.Sand),
            Vector(CellType.Sand, CellType.Sand, CellType.Sand))
        Board(cells)
    }

    def createNormalBoard: Board = {
        val cells = Vector(
            Vector(CellType.Sand, CellType.Base, CellType.Palm),
            Vector(CellType.Sand, CellType.Sand, CellType.Sand),
            Vector(CellType.Palm, CellType.Base, CellType.Sand))
        Board(cells)
    }
}
