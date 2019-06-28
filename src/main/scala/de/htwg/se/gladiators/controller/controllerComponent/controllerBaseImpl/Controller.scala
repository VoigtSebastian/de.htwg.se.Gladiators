package de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.{GameStatus, P1, P2}
import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import de.htwg.se.gladiators.controller.controllerComponent._
import CommandStatus._
import com.google.inject.{Guice, Inject}
import de.htwg.se.gladiators.GladiatorsModule
import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.util.UndoManager

import scala.swing.Publisher

class Controller () extends ControllerInterface with Publisher {

    val undoManager = new UndoManager
    var gameStatus: GameStatus = GameStatus.P1
    var commandStatus: CommandStatus = CommandStatus.IDLE
    var players = Array(Player("Player1"), Player("Player2"))
    var selectedCell: (Int, Int) = (0, 0)
    var selectedGlad: Gladiator = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
    var shop = Shop(10)
    val injector = Guice.createInjector(new GladiatorsModule)
    var playingField = PlayingField().createRandom(15)

    //playingField.createRandom(15)

    def cell(line: Int, row: Int): Cell = playingField.cell(line, row)

    def resetGame(): Unit = {
        playingField = PlayingField()
        gameStatus = GameStatus.P1
        commandStatus = CommandStatus.IDLE
        players = Array(Player("Player1"), Player("Player2"))
        selectedCell = (0, 0)
        selectedGlad = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
        shop = Shop(10)
    }

    def endTurn(): String = {
        playingField.resetGladiatorMoved()
        players(gameStatus.id).boughtGladiator = false
        players(gameStatus.id).credits += 1
        nextPlayer()
        publish(new GladChanged)
        "Turn successfully ended"
    }

    def createRandom(size: Int, palmRate: Int = 17): Unit = {
        playingField = playingField.createRandom(size, palmRate)
        //notifyObservers
        publish(new PlayingFieldChanged)
    }

    def getShop: String = shop.toString

    def printPlayingField(): String = {
        // notifyObservers
        playingField.toString +
            GameStatus.message(gameStatus) +
            "\n"
    }

    def addGladiator(line: Int, row: Int): Boolean = {
        if (players(gameStatus.id).boughtGladiator)
            return false

        if (playingField.cells(line)(row).cellType == CellType.PALM)
            return false

        if (checkGladiator(line, row))
            return false

        if (!baseArea(players(gameStatus.id)).contains((line, row)))
            return false

        if (selectedGlad.line == -2) {
            for ((g, i) <- shop.stock.zipWithIndex) {
                if (g == selectedGlad) {
                    shop.buy(i, players(gameStatus.id)) match {
                        case Some(g) =>
                            selectedGlad.line = line
                            selectedGlad.row = row
                            selectedGlad.player = players(gameStatus.id)
                            if (gameStatus == P1)
                                playingField = playingField.addGladPlayerOne(selectedGlad)
                            else if (gameStatus == P2)
                                playingField = playingField.addGladPlayerTwo(selectedGlad)
                            return true
                        case None => return false
                    }
                }
            }
            false
        } else {
            selectedGlad = shop.genGlad()
            selectedGlad.line = line
            selectedGlad.row = row
            selectedGlad.player = players(gameStatus.id)
            if (gameStatus == P1)
                playingField = playingField.addGladPlayerOne(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
            else if (gameStatus == P2)
                playingField = playingField.addGladPlayerTwo(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
            return true
        }

        false
    }

    def buyGladiator(index: Int, line: Int, row: Int): String = {
        if (players(gameStatus.id).boughtGladiator)
            return "You already purchased a unit this turn"

        if (playingField.cells(line)(row).cellType == CellType.PALM)
            return "You can not create units on palm"

        if (!baseArea(players(gameStatus.id)).contains((line, row)))
            return "You can not create a unit outside you base area"

        val glad = shop.buy(index, players(gameStatus.id))
        glad match {
            case Some(g) =>
                g.line = line
                g.row = row
                if (gameStatus == P1)
                    playingField.addGladPlayerOne(g)
                if (gameStatus == P2)
                    playingField.addGladPlayerTwo(g)
                publish(new GladChanged)
                "Gladiator " + g.toString + " added successfully"
            case None => "Please enter a legal index and make sure that you have enough credits!"
        }
    }

    def baseArea(player: Player): List[(Int, Int)] = {
        var base1: (Int, Int) = (0, 0)
        var area: List[(Int, Int)] = Nil
        if (player == players(0)) {
            base1 = (playingField.size - 1, playingField.size / 2)
            area = area ::: (base1._1 - 1, base1._2) :: Nil
        } else if (player == players(1)) {
            base1 = (0, playingField.size / 2)
            area = area ::: (base1._1 + 1, base1._2) :: Nil
        }

        area = area ::: (base1._1, base1._2 - 1) :: Nil
        area = area ::: (base1._1, base1._2 + 1) :: Nil

        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            area = area.filter(c => !(c._1 == g.line && c._2 == g.row))
        }
        area.filter(c => playingField.cells(c._1)(c._2).cellType != CellType.PALM)
    }

    def getBase(player: Player): (Int, Int) = {
        if (player == players(0)) {
            (playingField.size - 1, playingField.size / 2)
        } else {
            (0, playingField.size / 2)
        }
    }

    def gladiatorInfo(line: Int, row: Int): String = {
        playingField.gladiatorInfo(line: Int, row: Int) + " and is owned by " + players(gameStatus.id)
    }

    def isCoordinateLegal(line: Int, row: Int): Boolean = {
        if (line < playingField.size && line >= 0 && row < playingField.size && row >= 0)
            return true
        false
    }

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): (Boolean, String) = {
        val status: MoveType.MoveType = categorizeMove(line, row, lineDest, rowDest)

        status match {
            case MoveType.LEGAL_MOVE =>
                undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
                getGladiatorOption(lineDest, rowDest) match {
                    case Some(g) => g.moved = true
                    case None =>
                }
                publish(new GladChanged)
                (true, "Move successful")
            case _ => (false, MoveType.message(status))
        }
    }

    def toggleUnitStats(): Unit = {
        if (playingField.toggleUnitStats)
            playingField.toggleUnitStats = false
        else
            playingField.toggleUnitStats = true
    }

    def undoGladiator(): Unit = {
        undoManager.undoStep
    }

    def nextPlayer(): Unit = {
        if (gameStatus == P1)
            gameStatus = P2
        else
            gameStatus = P1
        publish(new GameStatusChanged)
    }


    def redoGladiator(): Unit = {
        undoManager.redoStep
    }

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): (Boolean, String) = {
        val status: MoveType.MoveType = categorizeMove(lineAttack, rowAttack, lineDest, rowDest)

        status match {
            case MoveType.ATTACK =>
                val ret = playingField.attack(getGladiator(lineAttack, rowAttack), getGladiator(lineDest, rowDest))
                getGladiatorOption(lineAttack, rowAttack) match {
                    case Some(g) =>
                        g.moved = true
                        (true, ret)
                    case None => (false, "Something went really wrong in attack")
                }
            case MoveType.GOLD =>
                val glad = getGladiator(lineAttack, rowAttack)
                glad.moved = true

                (true, mineGold(glad, lineDest, rowDest))
            case MoveType.BASE_ATTACK =>
                val glad = getGladiator(lineAttack, rowAttack)
                players(1 - gameStatus.id).baseHP -= glad.ap.toInt
                glad.moved = true
                if (players(1 - gameStatus.id).baseHP <= 0) {
                    publish(new GameOver)
                }
                (true, "Base of Player " + players(gameStatus.id).name + " has been attacked")
            case MoveType.LEGAL_MOVE => (false, "Please use the move command to move your units")
            case MoveType.BLOCKED => (false, "You can not attack your own units")
            case _ => (false, MoveType.message(status))
        }
    }

    def checkGladiator(line: Int, row: Int): Boolean = {
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                return true
        }
        false
    }

    def getGladiator(line: Int, row: Int): Gladiator = {
        var glad = GladiatorFactory.createGladiator(Int.MinValue, Int.MinValue, GladiatorType.SWORD, players(P1.id))
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                glad = g
        }
        glad
    }

    def getGladiatorOption(line: Int, row: Int): Option[Gladiator] = {
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                return Some(g)
        }
        None
    }

    def cellSelected(line: Int, row: Int): Unit = {

        if (!addGladiator(line, row)) {
            if (!moveGladiator(selectedCell._1, selectedCell._2, line, row)._1) {
                if (!attack(selectedCell._1, selectedCell._2, line, row)._1) {
                    selectedCell = (line, row)
                } else {
                    publish(new GladChanged)
                }
            } else {
                publish(new GladChanged)
            }
        } else {
            publish(new GladChanged)
        }
        selectedCell = (line, row)
        publish(new CellClicked)
        //selectedCell = (line, row)
        /*
        if (commandStatus == CommandStatus.IDLE) {
            selectedCell = (line, row)
            publish(new CellClicked)
        } else if (commandStatus == CommandStatus.CR) {
            addGladiator(line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        } else if (commandStatus == CommandStatus.MV) {
            moveGladiator(selectedCell._1, selectedCell._2, line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        } else if (commandStatus == CommandStatus.AT) {
            attack(selectedCell._1, selectedCell._2, line, row)
            commandStatus = CommandStatus.IDLE
            selectedCell = (line, row)
            publish(new GladChanged)
        }
        */
    }

    def changeCommand(commandStatus: CommandStatus): Controller = {
        this.commandStatus = commandStatus
        this
    }

    def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType = {
        if (!isCoordinateLegal(lineStart, rowStart) ||
            !isCoordinateLegal(lineDest, rowDest))
            return MoveType.ILLEGAL_MOVE

        val gladStartOption = getGladiatorOption(lineStart, rowStart)
        val gladDestOption = getGladiatorOption(lineDest, rowDest)

        gladStartOption match {
            case Some(gladiatorStart) =>
                if (gladiatorStart.player != players(gameStatus.id))
                    return MoveType.UNIT_NOT_OWNED_BY_PLAYER
                if (gladiatorStart.moved)
                    return MoveType.ALREADY_MOVED

                gladDestOption match {
                    case Some(gladiatorDest) =>
                        if (gladiatorStart.player == gladiatorDest.player)
                            return MoveType.BLOCKED
                        if (checkMovementPointsAttack(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                            MoveType.ATTACK
                        else
                            MoveType.INSUFFICIENT_MOVEMENT_POINTS
                    case None =>

                        if (playingField.cells(lineDest)(rowDest).cellType != CellType.PALM)
                            if (playingField.cells(lineDest)(rowDest).cellType == CellType.BASE)
                                if (checkMovementPointsAttack(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                                    if (gameStatus == P1 && lineDest == 0)
                                        MoveType.BASE_ATTACK
                                    else if (gameStatus == P2 && lineDest == playingField.size - 1)
                                        MoveType.BASE_ATTACK
                                    else
                                        MoveType.ILLEGAL_MOVE
                                else
                                    MoveType.ILLEGAL_MOVE
                            else if (playingField.cells(lineDest)(rowDest).cellType == CellType.GOLD)
                                if (checkMovementPointsAttack(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                                    MoveType.GOLD
                                else
                                    MoveType.ILLEGAL_MOVE
                            else if (checkMovementPoints(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                                MoveType.LEGAL_MOVE
                            else
                                MoveType.INSUFFICIENT_MOVEMENT_POINTS
                        else
                            MoveType.MOVE_TO_PALM
                    /*
                    if (playingField.cells(lineDest)(rowDest).cellType != CellType.PALM)
                        if (checkMovementPoints(gladiatorStart, lineStart, rowStart, lineDest, rowDest))
                            if (playingField.cells(lineDest)(rowDest).cellType == CellType.BASE)
                                if (gameStatus == P1 && lineDest == 0)
                                    MoveType.BASE_ATTACK
                                else if (gameStatus == P2 && lineDest == playingField.size - 1)
                                    MoveType.BASE_ATTACK
                                else
                                    MoveType.ILLEGAL_MOVE
                            else if (playingField.cells(lineDest)(rowDest).cellType == CellType.GOLD)
                                MoveType.GOLD
                            else
                                MoveType.LEGAL_MOVE
                        else
                            MoveType.INSUFFICIENT_MOVEMENT_POINTS
                    else
                        MoveType.MOVE_TO_PALM
                        */
                }
            case None => MoveType.UNIT_NOT_EXISTING
        }
    }

    def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean = {
        for (g <- list)
            if (g.row == row && g.line == line)
                return true
        false
    }

    def checkMovementPoints(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = {
        if (g.row == rowStart &&
            g.line == lineStart &&
            g.movementPoints >= (Math.abs(lineDest - lineStart) + Math.abs(rowDest - rowStart)))
            return true
        false
    }

    def checkMovementPointsAttack(g: Gladiator, lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): Boolean = {
        if (g.row == rowStart &&
            g.line == lineStart)
            g.gladiatorType match {
                case GladiatorType.SWORD | GladiatorType.TANK =>
                    if (1 >= (Math.abs(lineDest - lineStart) + Math.abs(rowDest - rowStart)))
                        return true
                case GladiatorType.BOW =>
                    if (2 >= (Math.abs(lineDest - lineStart) + Math.abs(rowDest - rowStart)))
                        return true
            }
        false
    }

    def mineGold(gladiatorAttack: Gladiator, line: Int, row: Int): String = {
        var player: Int = 0
        if (gladiatorAttack.player == players(0))
            player = 0
        else
            player = 1
        players(player).credits += (gladiatorAttack.ap / 10).toInt
        var randLine = scala.util.Random.nextInt(playingField.size - 4) + 2
        var randRow = scala.util.Random.nextInt(playingField.size)
        while (!checkCellEmpty(randLine, randRow)) {
            randLine = scala.util.Random.nextInt(playingField.size - 4) + 2
            randRow = scala.util.Random.nextInt(playingField.size)
        }
        playingField.cells(line)(row) = Cell(CellType.SAND)
        playingField.cells(randLine)(randRow) = Cell(CellType.GOLD)
        gladiatorAttack + " is goldmining"
    }

    def checkCellEmpty(line: Int, row: Int): Boolean = {
        if (playingField.cells(line)(row).cellType == CellType.SAND
            && !checkGladiator(line, row)) {
            true
        } else {
            false
        }
    }

}
