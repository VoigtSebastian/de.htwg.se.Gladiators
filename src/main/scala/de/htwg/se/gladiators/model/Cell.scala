package de.htwg.se.gladiators.model

case class Cell(cellType: Int) {

    def getCellType(): Int = {
        return cellType
    }

    override def toString(): String = {
        return "" + cellType;
    }

}
