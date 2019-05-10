package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.CellType.CellType

case class Cell(cellType: CellType) {

    override def toString: String = {
        "" + cellType.id
    }

}
