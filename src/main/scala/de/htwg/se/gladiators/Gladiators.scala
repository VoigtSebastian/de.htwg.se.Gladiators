package de.htwg.se.gladiators

import de.htwg.se.gladiators.model.{Cell, CellType, Gladiator, GladiatorType, PlayingField}
import de.htwg.se.sudoku.aview.Tui

object Gladiators {
    var tui = new Tui

    def main(args: Array[String]): Unit = {
        println("Gladiators")
        val cells = Array.ofDim[Cell](3, 3)

        var field = PlayingField(cells)
        field.createRandom(5)

        var glad: List[Gladiator] = List(Gladiator(0,0, 1.0,1.0,1.0, GladiatorType.MAGIC),
            Gladiator(1,0, 1.0,1.0,1.0, GladiatorType.SWORD))

        println("Line zero with gladiators: " + field.formatLine(0,glad) + "\n\n")
        
        var input: String = "h"
        do {
          input = readLine()
          field = tui.processInputLine(input, field)
          println("PlayingField:\n" + field.toString())
        } while (input != "q")
        
        println(field.toString())
        
    }
}
