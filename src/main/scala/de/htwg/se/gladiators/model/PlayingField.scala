package de.htwg.se.gladiators.model

import scala.util.matching.Regex

case class PlayingField(size: Integer = 15) {

    var gladiatorPlayer1: List[Gladiator] = List()
    var gladiatorPlayer2: List[Gladiator] = List()
    var cells: Array[Array[Cell]] = Array.ofDim[Cell](3, 3)
    var toggleUnitStats = true

    val SAND_BACKGROUND = "\033[103m"
    val PALM_BACKGROUND = "\033[43m"
    val BASE_BACKGROUND = "\033[101m"
    val UNIT_BACKGROUND = "\033[45m"
    val TEXT_COLOR_BLACK = "\33[97m"
    val RESET_ANSI_ESCAPE = "\033[0m"
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")

    createRandom(size)

    def setField(cells: Array[Array[Cell]]): PlayingField = {
        this.cells = cells
        this
    }

    override def toString: String = {
        var output = ""
        for (i <- cells.indices) {
            output += evalPrintLine(formatLine(i)) + "\n"
        }
        if (toggleUnitStats)
            return formatPlayingFieldAddStats(output)
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

    def formatLineAddGladiators(gladiator: Gladiator, retString: String, line: Int): String = {
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

    def resetGladiatorMoved(): Unit = {
        for (glad <- gladiatorPlayer1 ::: gladiatorPlayer2)
            glad.moved = false
    }

    def formatPlayingFieldAddStats(playingField: String): String = {
        var ret = playingField
        if (gladiatorPlayer1.nonEmpty) {
            ret = ret + "Gladiators of player one: \n"
            for (g <- gladiatorPlayer1)
                ret = ret + g.toString + "\n"
        }
        if (gladiatorPlayer2.nonEmpty) {
            ret = ret + "Gladiators of player two: \n"
            for (g <- gladiatorPlayer2)
                ret = ret + g.toString + "\n"
        }
        ret
    }

    def createRandom(length: Int, palmRate: Int = 17): PlayingField = {
        cells = Array.ofDim[Cell](length, length)
        for (i <- cells.indices) {
            for (j <- cells(i).indices) {
                //cells(i)(j) = Cell(scala.util.Random.nextInt(CellType.maxId - 1));
                val randInt = scala.util.Random.nextInt(100)
                if (randInt >= palmRate) {
                    cells(i)(j) = Cell(CellType.SAND)
                } else {
                    cells(i)(j) = Cell(CellType.PALM)
                }
            }
        }
        val goldInd = scala.util.Random.nextInt(length)
        cells(length / 2)(goldInd) = Cell(CellType.GOLD)
        cells(0)(length / 2) = Cell(CellType.BASE)
        cells(length - 1)(length / 2) = Cell(CellType.BASE)
        this
    }

    def addGladPlayerOne(gladiator: Gladiator): PlayingField = {
        gladiatorPlayer1 = gladiatorPlayer1 ::: gladiator :: Nil
        this
    }

    def addGladPlayerTwo(gladiator: Gladiator): PlayingField = {
        gladiatorPlayer2 = gladiatorPlayer2 ::: gladiator :: Nil
        this
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): PlayingField = {
        for (g <- gladiatorPlayer1 ::: gladiatorPlayer2) {
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


    def evalPrintLine(line: String): String = {
        var returnValue = ""
        for (c <- line) {
            c match {
                //gladiators
                case 'S' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    UNIT_BACKGROUND + " S " + RESET_ANSI_ESCAPE) //-> SWORD
                case 'B' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    UNIT_BACKGROUND + " B " + RESET_ANSI_ESCAPE) //-> BOW
                case 'T' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    UNIT_BACKGROUND + " T " + RESET_ANSI_ESCAPE) //-> TANK
                //cells
                case '0' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    SAND_BACKGROUND + " S " + RESET_ANSI_ESCAPE) //-> SAND
                case '1' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    PALM_BACKGROUND + " P " + RESET_ANSI_ESCAPE) //-> PALM
                case '2' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    BASE_BACKGROUND + " B " + RESET_ANSI_ESCAPE) //-> BASE
                case '3' => returnValue = returnValue + (TEXT_COLOR_BLACK +
                    BASE_BACKGROUND + " G " + RESET_ANSI_ESCAPE) //-> BASE
            }
        }
        returnValue
    }

    def attack(gladiatorAttack: Gladiator, gladiatorDest: Gladiator): String = {
        gladiatorDest.hp -= gladiatorAttack.ap
        if (gladiatorDest.hp <= 0) {
            gladiatorPlayer1 = gladiatorPlayer1.filter(g => g != gladiatorDest)
            gladiatorPlayer2 = gladiatorPlayer2.filter(g => g != gladiatorDest)
        }
        gladiatorAttack + " attackes " + gladiatorDest
    }

}

