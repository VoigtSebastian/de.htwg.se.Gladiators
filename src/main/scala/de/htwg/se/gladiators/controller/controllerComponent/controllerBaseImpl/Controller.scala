package de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.{ GameStatus, P1, P2 }
import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.GladiatorsModule
import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.model.fileIoComponent.FileIOInterface
import de.htwg.se.gladiators.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.util.{ Coordinate, UndoManager }
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.util.{ UpdateNameArgumentContainer, PlayerJsonSupport }

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.model.{ HttpEntity, HttpResponse, ContentTypes, MediaTypes, headers }
import akka.actor.ActorSystem
import akka.http.scaladsl.{ Http }
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.http.scaladsl.unmarshalling.Unmarshaller._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContextExecutor, Future }
import scala.util.Properties.envOrElse
import scala.swing.Publisher

import play.api.libs.json.{ JsValue, Json }
import java.util.concurrent.TimeUnit
import com.google.inject.{ Guice, Inject }
import CommandStatus._

class Controller @Inject() () extends ControllerInterface with Publisher with PlayerJsonSupport {

    var playingField: PlayingFieldInterface = PlayingField().createRandomCells(15)
    val undoManager = new UndoManager
    var gameStatus: GameStatus = GameStatus.P1
    var commandStatus: CommandStatus = CommandStatus.IDLE
    var players = Array(Player("Player 1"), Player("Player 2", enemyBaseLine = playingField.size - 1))
    var selectedCell: (Int, Int) = (0, 0)
    var selectedGlad: Gladiator = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
    var shop = Shop(10)
    val injector = Guice.createInjector(new GladiatorsModule)
    val kickOutTurns = 7
    var fileIo = injector.getInstance((classOf[FileIOInterface]))

    val port = envOrElse("PLAYER-SERVICE-PORT", "8081").toInt
    val domain = envOrElse("DOMAIN", "localhost")

    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    def cell(line: Int, row: Int): Cell = playingField.cell(line, row)

    def resetGame(): Unit = {
        playingField = playingField.resetPlayingField()
        gameStatus = GameStatus.P1
        commandStatus = CommandStatus.IDLE
        players = Array(Player("Player1"), Player("Player2"))
        selectedCell = (0, 0)
        selectedGlad = GladiatorFactory.createGladiator(-1, -1, GladiatorType.SWORD, players(gameStatus.id))
        shop = Shop(10)
    }

    def endTurn(): String = {
        shop.endTurn()
        shop.kickOut(kickOutTurns)
        playingField = playingField.resetGladiatorMoved()
        players(gameStatus.id) = players(gameStatus.id).updateBoughtGladiator(false).addCredits(1)
        nextPlayer()
        publish(new GladChanged)
        "Turn successfully ended"
    }

    def createRandom(size: Int, palmRate: Int = 17): Unit = {
        playingField = playingField.createRandomCells(size, palmRate)
        //notifyObservers
        publish(new PlayingFieldChanged)
    }

    def getShop: String = shop.toString

    def getCurrentPlayer: Player = players(gameStatus.id)

    def printPlayingField(): String = {
        playingField.toString +
            "\nSelected cell: " + selectedCell + "\n" +
            "Current Player: " + players(gameStatus.id).name + "\n"
    }

    def addGladiator(line: Int, row: Int): Boolean = {
        if (players(gameStatus.id).boughtGladiator)
            return false

        if (playingField.cells(line)(row).cellType == CellType.PALM)
            return false

        if (!baseArea(players(gameStatus.id)).contains((line, row)))
            return false

        getGladiatorOption(Coordinate(line, row)) match {
            case Some(g) => return false
            case None =>
        }

        if (selectedGlad.line == -2) {
            for ((g, i) <- shop.stock.zipWithIndex) {
                if (g._1 == selectedGlad) {
                    shop.buy(i, players(gameStatus.id)) match {
                        case Some(g) =>
                            var gladiator = g
                            selectedGlad = gladiator.assignPlayer(players(gameStatus.id))
                            selectedGlad = gladiator.move(line, row)
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
        }
        /*
        else {
            selectedGlad = shop.genGlad()
            selectedGlad.line = line
            selectedGlad.row = row
            selectedGlad.player = players(gameStatus.id)
            if (gameStatus == P1)
                playingField.addGladPlayerOne(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
            else if (gameStatus == P2)
                playingField.addGladPlayerTwo(GladiatorFactory.createGladiator(line, row, selectedGlad.gladiatorType, players(gameStatus.id)))
            return true
        }
        */

        false
    }

    def buyGladiator(index: Int, line: Int, row: Int): String = {
        if (players(gameStatus.id).boughtGladiator)
            return "You already purchased a unit this turn"

        if (playingField.cells(line)(row).cellType == CellType.PALM)
            return "You can not create units on palm"

        if (!baseArea(players(gameStatus.id)).contains((line, row)))
            return "You can not create a unit outside you base area"

        var glad = shop.buy(index, players(gameStatus.id))
        glad match {
            case Some(g) =>
                var gladiator = g
                gladiator = gladiator.move(line, row)
                if (gameStatus == P1)
                    playingField = playingField.addGladPlayerOne(gladiator)
                if (gameStatus == P2)
                    playingField = playingField.addGladPlayerTwo(gladiator)
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

    def moveGladiator(line: Int, row: Int, lineDest: Int, rowDest: Int): (Boolean, String) = {
        val status: MoveType.MoveType = categorizeMove(line, row, lineDest, rowDest)

        status match {
            case MoveType.LEGAL_MOVE =>
                undoManager.doStep(new MoveGladiatorCommand(line, row, lineDest, rowDest, this))
                getGladiatorOption(Coordinate(lineDest, rowDest)) match {
                    case Some(g) =>
                        var gladiator = g
                        gladiator = gladiator.updateMoved(true)
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
        publish(new GladChanged)
    }

    def attack(lineAttack: Int, rowAttack: Int, lineDest: Int, rowDest: Int): (Boolean, String) = {
        val status: MoveType.MoveType = categorizeMove(lineAttack, rowAttack, lineDest, rowDest)

        status match {
            case MoveType.ATTACK =>
                playingField = playingField.attack(getGladiator(lineAttack, rowAttack), getGladiator(lineDest, rowDest))
                getGladiatorOption(Coordinate(lineAttack, rowAttack)) match {
                    case Some(g) =>
                        var gladiator = g
                        gladiator = gladiator.updateMoved(true)
                        (true, "GladiatorAttack")
                    case None => (false, "Something went really wrong in attack")
                }
            case MoveType.GOLD =>
                var glad = getGladiator(lineAttack, rowAttack)
                glad = glad.updateMoved(true)

                (true, mineGold(glad, lineDest, rowDest))
            case MoveType.BASE_ATTACK =>
                var glad = getGladiator(lineAttack, rowAttack)
                players(1 - gameStatus.id) = players(1 - gameStatus.id).baseAttacked(glad.ap.toInt)
                glad = glad.updateMoved(true)
                if (players(1 - gameStatus.id).baseHP <= 0) {
                    gameOver(players(gameStatus.id))
                }
                (true, "Base of Player " + players(gameStatus.id).name + " has been attacked")
            case MoveType.LEGAL_MOVE => (false, "Please use the move command to move your units")
            case MoveType.BLOCKED => (false, "You can not attack your own units")
            case _ => (false, MoveType.message(status))
        }
    }

    def getGladiator(line: Int, row: Int): Gladiator = {
        var glad = GladiatorFactory.createGladiator(Int.MinValue, Int.MinValue, GladiatorType.SWORD, players(P1.id))
        for (g <- playingField.gladiatorPlayer1 ::: playingField.gladiatorPlayer2) {
            if (g.line == line && g.row == row)
                glad = g
        }
        glad
    }

    def getGladiatorOption(position: Coordinate): Option[Gladiator] = {
        playingField.getGladiatorOption(position)
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
    }

    def changeCommand(commandStatus: CommandStatus): Controller = {
        this.commandStatus = commandStatus
        this
    }

    def categorizeMove(lineStart: Int, rowStart: Int, lineDest: Int, rowDest: Int): MoveType = {
        playingField.checkMoveType(Coordinate(lineStart, rowStart), Coordinate(lineDest, rowDest), getCurrentPlayer)
    }

    def isGladiatorInList(list: List[Gladiator], line: Int, row: Int): Boolean = {
        for (g <- list)
            if (g.row == row && g.line == line)
                return true
        false
    }

    def checkMovementPointsAttack(g: Gladiator, lineStart: Int, rowStart: Int): List[Coordinate] = {
        playingField.checkMovementPointsAttack(g, Coordinate(lineStart, rowStart))
    }

    def mineGold(gladiatorAttack: Gladiator, line: Int, row: Int): String = {
        var playerId: Int = 0
        if (gladiatorAttack.player.name == players(0).name) {
            playerId = 0
        } else {
            playerId = 1
        }
        players(playerId) = players(playerId).addCredits((gladiatorAttack.ap / 10).toInt)
        var randLine = scala.util.Random.nextInt(playingField.size - 4) + 2
        var randRow = scala.util.Random.nextInt(playingField.size)
        while (!playingField.checkCellEmpty(Coordinate(randLine, randRow))) {
            randLine = scala.util.Random.nextInt(playingField.size - 4) + 2
            randRow = scala.util.Random.nextInt(playingField.size)
        }
        playingField.cells(line)(row) = Cell(CellType.SAND)
        playingField.cells(randLine)(randRow) = Cell(CellType.GOLD)
        gladiatorAttack + " is goldmining"
    }

    def save(): Unit = {
        fileIo.save(playingField)
    }

    def load(): Unit = {
        val temppf = fileIo.load
        playingField = playingField.updateCells(temppf.cells)
        publish(new PlayingFieldChanged)
    }

    def playingFieldToHtml: String = playingField.toHtml

    def setPlayerName(ind: Int, name: String): Unit = {
        val response = Http().singleRequest(Put(
            s"http://$domain:$port/gladiators/player/updateName",
            UpdateNameArgumentContainer(players(ind), name)))
        val future = response.flatMap(r => Unmarshal[HttpEntity](r.entity.withContentType(ContentTypes.`application/json`)).to[Player])
        players(ind) = Await.result(future, Duration(1, TimeUnit.SECONDS))
    }

    def gameOver(winner: Player): Unit = {
        val response = Http().singleRequest(Put(
            s"http://$domain:$port/gladiators/player/create",
            winner))
        val future = response.flatMap(r => Unmarshal[HttpEntity](r.entity.withContentType(ContentTypes.`application/json`)).to[Player])
        val player = Await.result(future, Duration(1, TimeUnit.SECONDS))
        println(player)
        publish(new GameOver)
    }

}
