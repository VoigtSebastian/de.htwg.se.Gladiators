package de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl

import java.security.KeyStore.TrustedCertificateEntry

import com.google.inject.Inject
import de.htwg.se.gladiators.controller.controllerComponent.MoveType
import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import de.htwg.se.gladiators.model.CellType.CellType
import de.htwg.se.gladiators.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.gladiators.model.{ Cell, CellType, Gladiator, GladiatorType }
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.playerModule.model.playerComponent.playerBaseImplementation.Player
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

import scala.util.matching.Regex

case class PlayingField @Inject() (size: Integer = 15, gladiatorPlayer1: List[Gladiator] = List(), gladiatorPlayer2: List[Gladiator] = List(), cells: Array[Array[Cell]] = Array.ofDim[Cell](15, 15)) extends PlayingFieldInterface {

    var toggleUnitStats = true

    val SAND_BACKGROUND = "\u001b[103m"
    val PALM_BACKGROUND = "\u001b[43m"
    val BASE_BACKGROUND = "\u001b[101m"
    val UNIT_BACKGROUND = "\u001b[45m"
    val TEXT_COLOR_BLACK = "\u001b[97m"
    val RESET_ANSI_ESCAPE = "\u001b[0m"
    val REGEX_COMMANDS = new Regex("([a-zA-Z]+)|([0-9]+)")

    def updateCells(cells: Array[Array[Cell]]): PlayingField = {
        this.copy(cells = cells)
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

    def resetGladiatorMoved(): PlayingField = {
        var gladiatorPlayer1New = gladiatorPlayer1
        var gladiatorPlayer2New = gladiatorPlayer2
        gladiatorPlayer1New = gladiatorPlayer1New.map(glad => glad.updateMoved(false))
        gladiatorPlayer2New = gladiatorPlayer2New.map(glad => glad.updateMoved(false))

        this.copy(gladiatorPlayer1 = gladiatorPlayer1New, gladiatorPlayer2 = gladiatorPlayer2New)
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

    def createRandomCells(length: Int, palmRate: Int = 17): PlayingField = {
        var cellsNew = Array.ofDim[Cell](length, length)
        for (i <- cellsNew.indices) {
            for (j <- cellsNew(i).indices) {
                //cells(i)(j) = Cell(scala.util.Random.nextInt(CellType.maxId - 1));
                val randInt = scala.util.Random.nextInt(100)
                if (randInt >= palmRate) {
                    cellsNew(i)(j) = Cell(CellType.SAND)
                } else {
                    cellsNew(i)(j) = Cell(CellType.PALM)
                }
            }
        }
        val goldInd = scala.util.Random.nextInt(length)
        cellsNew(length / 2)(goldInd) = Cell(CellType.GOLD)
        cellsNew(0)(length / 2) = Cell(CellType.BASE)
        cellsNew(length - 1)(length / 2) = Cell(CellType.BASE)
        this.copy(cells = cellsNew)
    }

    def addGladPlayerOne(gladiator: Gladiator): PlayingField = {
        val gladiatorPlayer1New = gladiator :: gladiatorPlayer1
        this.copy(gladiatorPlayer1 = gladiatorPlayer1New)
    }

    def addGladPlayerTwo(gladiator: Gladiator): PlayingField = {
        val gladiatorPlayer2New = gladiator :: gladiatorPlayer2
        this.copy(gladiatorPlayer2 = gladiatorPlayer2New)
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): PlayingField = {
        val filter = (gladiator: Gladiator) => gladiator.line == line && gladiator.row == row
        val newGlad = (gladiatorPlayer1 ::: gladiatorPlayer2).filter(filter).head.move(lineDest, rowDest)

        gladiatorPlayer1.exists(filter) match {
            case true => this.copy(gladiatorPlayer1 = newGlad :: gladiatorPlayer1.filter(g => g.line != line || g.row != row))
            case false => this.copy(gladiatorPlayer2 = newGlad :: gladiatorPlayer2.filter(g => g.line != line || g.row != row))
        }
    }

    def getSize: Integer = {
        this.cells.length
    }

    def cell(line: Int, row: Int): Cell = cells(line)(row)

    def cellAtCoordinate(coordinate: Coordinate): Cell = cell(coordinate.line, coordinate.row)

    def checkCellEmpty(coord: Coordinate): Boolean = {
        if (cells(coord.line)(coord.row).cellType == CellType.SAND) {
            getGladiatorOption(coord) match {
                case None => true
                case Some(g) => false
            }
        } else {
            false
        }
    }

    def checkCellWalk(coord: Coordinate): Boolean = {
        if (cells(coord.line)(coord.row).cellType == CellType.SAND
            || cells(coord.line)(coord.row).cellType == CellType.BASE) {
            getGladiatorOption(coord) match {
                case None => true
                case Some(g) => false
            }
        } else {
            false
        }
    }

    def gladiatorInfo(line: Int, row: Int): String = {
        val glad = (gladiatorPlayer1 ::: gladiatorPlayer2).filter(g => g.line == line && g.row == row)
        glad.length match {
            case 0 => ""
            case _ => glad.head.toString()
        }
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

    def attack(gladiatorAttack: Gladiator, gladiatorDest: Gladiator): PlayingField = {
        val newGladiator = gladiatorDest.getAttacked(gladiatorAttack.ap)

        gladiatorPlayer1.contains(gladiatorDest) match {
            case true => this.copy(gladiatorPlayer1 = (newGladiator :: gladiatorPlayer1).filter(g => g != gladiatorDest && g.hp > 0))
            case false => this.copy(gladiatorPlayer2 = (newGladiator :: gladiatorPlayer2).filter(g => g != gladiatorDest && g.hp > 0))
        }
    }

    def setGladiator(line: Int, row: Int, glad: Gladiator): PlayingField = {
        gladiatorPlayer1.exists(g => g.line == line && g.row == row) match {
            case true => this.copy(gladiatorPlayer1 = glad :: gladiatorPlayer1.filter(g => g != glad))
            case false => this.copy(gladiatorPlayer2 = glad :: gladiatorPlayer2.filter(g => g != glad))
        }
    }

    def resetPlayingField(): PlayingField = {
        this.copy(gladiatorPlayer1 = List(), gladiatorPlayer2 = List())
    }

    def setCell(line: Int, row: Int, cellType: CellType): Unit = {
        cells(line)(row) = Cell(cellType)
    }

    def checkMoveType(startPosition: Coordinate, destinationPosition: Coordinate, currentPlayer: PlayerInterface): MoveType = {
        if (!isCoordinateLegal(startPosition) || !isCoordinateLegal(destinationPosition))
            return MoveType.MOVE_OUT_OF_BOUNDS
        val startGlad = getGladiatorOption(startPosition)
        val destGlad = getGladiatorOption(destinationPosition)

        (startGlad, destGlad) match {
            case (None, None) | (None, Some(_)) => MoveType.UNIT_NOT_EXISTING
            case (Some(attackingGladiator), None) => checkMoveOrBaseAttack(attackingGladiator, startPosition, destinationPosition, currentPlayer)
            case (Some(attackingGladiator), Some(attackedGladiator)) => checkAttackValid(attackingGladiator, attackedGladiator, startPosition, destinationPosition, currentPlayer)
        }
    }

    def isCoordinateLegal(coordinate: Coordinate): Boolean = {
        coordinate.line < size && coordinate.line >= 0 && coordinate.row < size && coordinate.row >= 0
    }

    def checkAttackValid(attackingGladiator: Gladiator, target: Gladiator, attackingPosition: Coordinate, targetPosition: Coordinate, currentPlayer: PlayerInterface): MoveType = {
        if (attackingGladiator.moved)
            return MoveType.ALREADY_MOVED
        if (attackingGladiator.player != currentPlayer)
            return MoveType.UNIT_NOT_OWNED_BY_PLAYER
        if (target.player == currentPlayer)
            return MoveType.BLOCKED
        if (checkMovementPointsAttack(attackingGladiator, attackingPosition, targetPosition))
            MoveType.ATTACK
        else
            MoveType.INSUFFICIENT_MOVEMENT_POINTS
    }

    def checkMoveOrBaseAttack(gladiator: Gladiator, startCoordinate: Coordinate, targetCoordinate: Coordinate, currentPlayer: PlayerInterface): MoveType = {
        if (gladiator.moved)
            return MoveType.ALREADY_MOVED
        if (gladiator.player != currentPlayer)
            return MoveType.UNIT_NOT_OWNED_BY_PLAYER
        if (!checkMovementPointsMove(gladiator, startCoordinate, targetCoordinate))
            return MoveType.INSUFFICIENT_MOVEMENT_POINTS
        cellAtCoordinate(targetCoordinate).cellType match {
            case CellType.PALM => MoveType.MOVE_TO_PALM
            case CellType.SAND => MoveType.LEGAL_MOVE
            case CellType.GOLD => MoveType.GOLD
            case CellType.BASE => checkBaseAttack(startCoordinate, targetCoordinate, gladiator, currentPlayer)
        }
    }

    def checkBaseAttack(start: Coordinate, destination: Coordinate, gladiator: Gladiator, currentPlayer: PlayerInterface): MoveType = {
        ((destination.line == currentPlayer.enemyBaseLine) && checkMovementPointsAttack(gladiator, start, destination)) match {
            case true => MoveType.BASE_ATTACK
            case false => MoveType.OWN_BASE
        }

    }

    def getGladiatorOption(position: Coordinate): Option[Gladiator] = {
        for (g <- gladiatorPlayer1 ::: gladiatorPlayer2) {
            if (g.line == position.line && g.row == position.row)
                return Some(g)
        }
        None
    }

    def checkMovementPointsAttack(g: Gladiator, startPosition: Coordinate, destination: Coordinate): Boolean = {
        if (g.row == startPosition.row && g.line == startPosition.line)
            g.gladiatorType match {
                case GladiatorType.SWORD | GladiatorType.TANK =>
                    if (1 >= (Math.abs(destination.line - startPosition.line) + Math.abs(destination.row - startPosition.row)))
                        return true
                case GladiatorType.BOW =>
                    if (2 >= (Math.abs(destination.line - startPosition.line) + Math.abs(destination.row - startPosition.row)))
                        return true
            }
        false
    }

    def checkMovementPointsMove(g: Gladiator, startPosition: Coordinate, destination: Coordinate): Boolean = {
        return getValidMoveCoordinates(g, startPosition).exists(coord => coord == destination)
    }

    def getValidMoveCoordinates(g: Gladiator, startPosition: Coordinate): List[Coordinate] = {
        var validCells: List[Coordinate] = List()
        getValidMoveCoordinatesHelper(startPosition, 1, g.movementPoints, List()).foreach(coord =>
            if (!validCells.exists(item => item == coord._1) && checkCellEmpty(coord._1)) {
                validCells = coord._1 :: validCells
            })
        validCells
    }

    def getValidMoveCoordinatesHelper(curr: Coordinate, dist: Int, maxDist: Double, validCells: List[(Coordinate, Int)]): List[(Coordinate, Int)] = {
        var currValidCells = validCells
        var nextCoordinates: List[Coordinate] = List(
            Coordinate(curr.line, curr.row - 1),
            Coordinate(curr.line, curr.row + 1),
            Coordinate(curr.line - 1, curr.row),
            Coordinate(curr.line + 1, curr.row))
        nextCoordinates.foreach(next => {
            if (isCoordinateLegal(next)
                && checkCellWalk(next)
                && !currValidCells.exists(item => item._1 == next && item._2 <= dist)) {

                currValidCells = (next, dist) :: currValidCells
                if (dist + 1 <= maxDist) {
                    currValidCells = currValidCells ::: getValidMoveCoordinatesHelper(next, dist + 1, maxDist, currValidCells) //recursion
                }
            }
        })
        currValidCells
    }

    def toHtml: String = {
        var cellsHtml = ""
        for (i <- cells.indices) {
            cellsHtml += formatLine(i) + "</br>"
        }
        "</p>" + cellsHtml + "</p>"
    }
}
