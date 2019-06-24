package de.htwg.se.gladiators.model.model.model

import de.htwg.se.gladiators.model._
import org.scalatest.{Matchers, WordSpec}

class PlayingFieldSpec extends WordSpec with Matchers {
    "The Playing Field" when {
        val cells = Array.ofDim[Cell](3, 3)
        cells(0)(0) = Cell(CellType.BASE)
        cells(0)(1) = Cell(CellType.SAND)
        cells(0)(2) = Cell(CellType.PALM)
        cells(1)(0) = Cell(CellType.SAND)
        cells(1)(1) = Cell(CellType.SAND)
        cells(1)(2) = Cell(CellType.PALM)
        cells(2)(0) = Cell(CellType.SAND)
        cells(2)(1) = Cell(CellType.SAND)
        cells(2)(2) = Cell(CellType.BASE)

        val SAND_BACKGROUND = "\033[103m"
        val PALM_BACKGROUND = "\033[43m"
        val BASE_BACKGROUND = "\033[101m"
        val UNIT_BACKGROUND = "\033[45m"
        val TEXT_COLOR_BLACK = "\33[97m"
        val RESET_ANSI_ESCAPE = "\033[0m"

        var field = PlayingField()
        field = field.setField(cells)

        var glad1 = GladiatorFactory.createGladiator(0, 0, GladiatorType.SWORD, Player())
        var glad2 = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, Player())

        "new" should {
            print(field.formatLine(0))
            "have a nice String representation" in {
                field.formatLine(0) should be("201")
                field.formatLine(1) should be("001")
                field.formatLine(2) should be("002")
            }
        }

        "can add gladiators" in {
            field.addGladPlayerOne(glad1)
            field.addGladPlayerTwo(glad2)

        }

        "can tell a gladiator to attack another" in {
            field.attack(glad1, glad2).contains("attackes") should be (true)
        }
    }
}
