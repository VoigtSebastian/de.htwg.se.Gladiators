package de.htwg.se.gladiators.model.model

import de.htwg.se.gladiators.model.{Cell, CellType, Player, PlayingField}
import org.scalatest.{Matchers, WordSpec}

class PlayingFieldSpec extends WordSpec with Matchers{
  "The Playing Field" when { "new" should {
    val cells = Array.ofDim[Cell](3, 3)
    cells(0)(0) = Cell(CellType.BASE.id)
    cells(0)(1) = Cell(CellType.SAND.id)
    cells(0)(2) = Cell(CellType.WATER.id)
    cells(1)(0) = Cell(CellType.SAND.id)
    cells(1)(1) = Cell(CellType.SAND.id)
    cells(1)(2) = Cell(CellType.WATER.id)
    cells(2)(0) = Cell(CellType.SAND.id)
    cells(2)(1) = Cell(CellType.SAND.id)
    cells(2)(2) = Cell(CellType.BASE.id)

    val field = PlayingField(cells);

    "have a nice String representation" in {
      field.toString should be("0 1 3 \n1 1 3 \n1 1 0 \n")
    }
  }}
}
