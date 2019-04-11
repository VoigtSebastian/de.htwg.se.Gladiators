package de.htwg.gladiators

import de.htwg.se.gladiators.model.CellType
import de.htwg.se.gladiators.model.PlayingField
import de.htwg.se.gladiators.model.Cell

object Gladiators {

    def main(args: Array[String]): Unit = {
        println("Gladiators");
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
        println(field.toString())
    }
}
