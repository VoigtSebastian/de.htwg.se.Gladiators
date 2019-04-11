package de.htwg.se.gladiators.model

case class playingField(cells: Array[Array[Cell]]) {
    def print(): Unit = {
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                //print(cells(i)(j).getCellType() + " ")
            }
            println()
        }
    }

}
