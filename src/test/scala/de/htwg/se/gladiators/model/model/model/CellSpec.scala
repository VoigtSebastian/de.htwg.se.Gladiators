package de.htwg.se.gladiators.model.model.model

import de.htwg.se.gladiators.model.{Cell, CellType}
import org.scalatest.{Matchers, WordSpec}

class CellSpec extends WordSpec with Matchers {
    "A Cell" when {
        "new" should {
            val cell = Cell(CellType.PALM.id)
            "have a type" in {
                cell.cellType should be(CellType.PALM.id)
            }
            "have a nice String representation" in {
                cell.toString should be("1")
            }
        }
    }
}
