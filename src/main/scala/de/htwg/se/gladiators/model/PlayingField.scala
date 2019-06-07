package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.controller.GameStatus._


case class PlayingField(size: Integer = 7) {

    var gladiatorPlayer1: List[Gladiator] = List()
    var gladiatorPlayer2: List[Gladiator] = List()
    var cells: Array[Array[Cell]] = Array.ofDim[Cell](3,3)

    createRandom(size)

    def setField(cells: Array[Array[Cell]]): PlayingField = {
        this.cells = cells
        this
    }

    override def toString: String = {
        var output = ""
        for(i <- cells.indices) {
            output += TuiEvaluator.evalPrintLine(formatLine(i)) + "\n"
        }
        output
    }

    def formatLine(line: Int): String = {
        var ret = ""
        for (i <- cells(line).indices) { //iterate of cells in line
            ret += cells(line)(i).cellType.id
        }
        for (gladiator <- gladiatorPlayer1)
            ret = formatLineAddGladiators(gladiator, ret, line)
        for (gladiator <- gladiatorPlayer2)
            ret = formatLineAddGladiators(gladiator, ret, line)
        ret
    }

    def formatLineAddGladiators (gladiator: Gladiator, retString: String, line: Int): String = {
        var ret = retString
        if (gladiator.line == line) {
            val currentCellType = cells(line)(gladiator.row).cellType
            if (!(currentCellType == CellType.BASE))
                gladiator.gladiatorType match {
                    case GladiatorType.TANK => ret = ret.substring(0, gladiator.row) +
                        'T' + ret.substring(gladiator.row + 1)
                    case GladiatorType.BOW => ret = ret.substring(0, gladiator.row) +
                        'B' + ret.substring(gladiator.row + 1)
                    case GladiatorType.SWORD => ret = ret.substring(0, gladiator.row) +
                        'S' + ret.substring(gladiator.row + 1)
                }
        }
        ret
    }

    def createRandom(length: Int): PlayingField = {
        cells = Array.ofDim[Cell](length, length)
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                //cells(i)(j) = Cell(scala.util.Random.nextInt(CellType.maxId - 1));
                val randInt = scala.util.Random.nextInt(100)
                if (randInt < 83) {
                    cells(i)(j) = Cell(CellType.SAND)
                } else {
                    cells(i)(j) = Cell(CellType.PALM)
                }
            }
        }
        cells(0)(length / 2) = Cell(CellType.BASE)
        cells(length - 1)(length / 2) = Cell(CellType.BASE)
        this
    }

    def createGladiator(gladiator: Gladiator, gameStatus: GameStatus): PlayingField = {
        if (gameStatus == P1)
            gladiatorPlayer1 = gladiatorPlayer1 ::: gladiator :: Nil
        else
            gladiatorPlayer2 = gladiatorPlayer2 ::: gladiator ::Nil
        this
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): PlayingField = {
        val gladiator: Gladiator = null
        for (g <- gladiatorPlayer1) {
            // find gladiator
            if (g.row == row && g.line == line) {
                g.move(lineDest, rowDest)
            }
        }
        this
    }

    def getSize: Integer = {
        this.cells.length
    }

    def cell(line: Int, row: Int): Cell = cells(line)(row)

    def gladiatorInfo(line: Int, row: Int): String = {
        var ret = ""
        for (glad <- gladiatorPlayer1)
            if (glad.line == line && glad.row == row) {
                ret = glad.toString()
                return ret
            }
        for (glad <- gladiatorPlayer2)
            if (glad.line == line && glad.row == row)
                ret = glad.toString()

        ret
    }

    def attack(line: Int, row: Int, lineDest: Int, rowDest: Int, gameStatus: GameStatus): String = {
        println("A debug message")
        ""
    }
}

