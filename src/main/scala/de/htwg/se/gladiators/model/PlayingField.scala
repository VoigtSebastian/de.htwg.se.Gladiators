package de.htwg.se.gladiators.model


case class PlayingField(cells: Array[Array[Cell]]) {
     override def toString(): String = {
        var str = ""
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                str = str + cells(i)(j).getCellType()
            }
            str += "\n"
        }
         str
    }

    def formatLine(line: Int, gladiators: List[Gladiator]): String = {
        var ret = ""

        for (i <- cells(line).indices) { //iterate of cells in line
            ret += cells(line)(i).cellType
        }
        println("Ausgabe wenn gladiator sich in momentaner line befindet")
        for (gladiator <- gladiators) //iterate over all gladiators
            if (gladiator.y == line) {
                ret = ret.substring(0, gladiator.x) + gladiator.gladiatorType.id + ret.substring(gladiator.x + 1)
            }
        //ret(gladiator.x) = gladiator.gladiatorType.id.toChar

        ret
    }
}
