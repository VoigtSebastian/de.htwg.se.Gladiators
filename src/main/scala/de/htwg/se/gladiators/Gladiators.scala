package de.htwg.se.gladiators

import de.htwg.se.gladiators.model.{Cell, CellType, Gladiator, GladiatorType, PlayingField}

object Gladiators {

    def main(args: Array[String]): Unit = {
        println("Gladiators")
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
        val field = PlayingField(cells)

        var glad: List[Gladiator] = List(Gladiator(0,0, 1.0,1.0,1.0, GladiatorType.MAGIC),
            Gladiator(1,0, 1.0,1.0,1.0, GladiatorType.SWORD))

        println("Line zero with gladiators: " + field.formatLine(0,glad) + "\n\n")

        println(field.toString())

    }
}
