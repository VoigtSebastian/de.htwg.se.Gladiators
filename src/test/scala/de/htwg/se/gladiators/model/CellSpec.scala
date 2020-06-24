package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CellSpec extends AnyWordSpec with Matchers {
    "A Cell" when {
        "new" should {
            val cell = Cell(CellType.PALM)
            "have a type" in {
                cell.cellType should be(CellType.PALM)
            }
            "have a nice String representation" in {
                cell.toString should be("1")
            }
        }
    }
}
