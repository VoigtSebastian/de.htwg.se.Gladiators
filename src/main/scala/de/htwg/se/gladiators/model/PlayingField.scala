package de.htwg.se.gladiators.model


case class PlayingField(var cells: Array[Array[Cell]]) {

    var glad: List[Gladiator] = List()

    override def toString(): String = {
        var output = ""
        for(i <- cells.indices) {
            output += formatLine(i) + "\n"
        }
        output
    }

    def formatLine(line: Int): String = {
        var ret = ""

        for (i <- cells(line).indices) { //iterate of cells in line
            ret += cells(line)(i).cellType
        }
        //println("Ausgabe wenn gladiator sich in momentaner line befindet")
        for (gladiator <- glad) //iterate over all gladiators
            if (gladiator.line == line) {
                ret = ret.substring(0, gladiator.row) + (gladiator.gladiatorType.id  + 3) + ret.substring(gladiator.row + 1)
            }
        //ret(gladiator.x) = gladiator.gladiatorType.id.toChar
        ret
    }

    def createRandom(length: Int): PlayingField = {
        cells = Array.ofDim[Cell](length, length)
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                //cells(i)(j) = Cell(scala.util.Random.nextInt(CellType.maxId - 1));
                val randInt = scala.util.Random.nextInt(100)
                if (randInt < 83) {
                    cells(i)(j) = Cell(CellType.SAND.id)
                } else {
                    cells(i)(j) = Cell(CellType.PALM.id)
                }
            }
        }
        cells(0)(length / 2) = Cell(CellType.BASE.id)
        cells(length - 1)(length / 2) = Cell(CellType.BASE.id)
        this
    }

    def createGladiator(gladiator: Gladiator): PlayingField = {
        glad = gladiator :: Nil
        this
    }
}
