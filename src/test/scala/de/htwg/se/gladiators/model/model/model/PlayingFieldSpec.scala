package de.htwg.se.gladiators.model.model.model

import de.htwg.se.gladiators.model.{Cell, CellType, PlayingField}
import org.scalatest.{Matchers, WordSpec}

class PlayingFieldSpec extends WordSpec with Matchers{
  "The Playing Field" when { "new" should {
    val cells = Array.ofDim[Cell](3, 3)
    cells(0)(0) = Cell(CellType.BASE.id)
    cells(0)(1) = Cell(CellType.SAND.id)
    cells(0)(2) = Cell(CellType.PALM.id)
    cells(1)(0) = Cell(CellType.SAND.id)
    cells(1)(1) = Cell(CellType.SAND.id)
    cells(1)(2) = Cell(CellType.PALM.id)
    cells(2)(0) = Cell(CellType.SAND.id)
    cells(2)(1) = Cell(CellType.SAND.id)
    cells(2)(2) = Cell(CellType.BASE.id)

    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"

    val field = PlayingField()
    field.setField(cells)
    print(field.formatLine(0))
    "have a nice String representation" in {
      field.formatLine(0) should be("201")
      field.formatLine(1) should be ("001")
      field.formatLine(2) should be("002")
    }
  }}
}
