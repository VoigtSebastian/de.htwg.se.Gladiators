package de.htwg.se.gladiators.model.model

import de.htwg.se.gladiators.model.CellType
import de.htwg.se.gladiators.model.Cell
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
    "A Cell" when {
        "new" should {
            val cell = Cell(CellType.WATER.id)
            "have a type" in {
                cell.cellType should be(CellType.WATER.id)
            }
            "have a nice Int representation" in {
                cell.toString should be("Your Name")
            }
        }
    }
}
