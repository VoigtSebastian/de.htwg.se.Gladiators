package de.htwg.se.gladiators.model


case class PlayingField(var cells: Array[Array[Cell]]) {
    val MAX_PERCENT = 100
    val PROBABILITY_OF_SAND = 75

    override def toString: String = {
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
        for (gladiator <- gladiators) { //iterate over all gladiators
            if (gladiator.line == line) {
                val currentCellType = cells(line)(gladiator.row).cellType
                if (!(currentCellType == CellType.BASE.id))
                    gladiator.gladiatorType match {
                        case GladiatorType.MAGIC => ret = ret.substring(0, gladiator.row) +
                            'M' + ret.substring(gladiator.row + 1)
                        case GladiatorType.BOW => ret = ret.substring(0, gladiator.row) +
                            'B' + ret.substring(gladiator.row + 1)
                        case GladiatorType.SWORD => ret = ret.substring(0, gladiator.row) +
                            'S' + ret.substring(gladiator.row + 1)
                    }
            }
        }
        ret
    }

    def createRandom(length: Int): PlayingField = {
        val cells = Array.ofDim[Cell](length, length)
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                val randInt = scala.util.Random.nextInt(MAX_PERCENT)
                if (randInt < PROBABILITY_OF_SAND) {
                    cells(i)(j) = Cell(CellType.SAND.id)
                } else {
                    cells(i)(j) = Cell(CellType.PALM.id)
                }
            }
        }
        cells(0)(length / 2) = Cell(CellType.BASE.id)
        cells(length - 1)(length / 2) = Cell(CellType.BASE.id)
        PlayingField(cells)
    }
}
