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
        }
    }

    def createSandBoard: Board = {
        val cells = Vector(
            Vector(CellType.Sand, CellType.Sand, CellType.Sand),
            Vector(CellType.Sand, CellType.Sand, CellType.Sand),
            Vector(CellType.Sand, CellType.Sand, CellType.Sand))
        Board(cells)
    }
}
