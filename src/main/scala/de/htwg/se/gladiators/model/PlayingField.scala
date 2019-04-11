package de.htwg.se.gladiators.model

case class PlayingField(cells: Array[Array[Cell]]) {
    override def toString(): String = {
        var str = "";
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                str = str + cells(i)(j).getCellType() + " ";
            }
            str += "\n";
        }
        return str;
    }

}
