package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.{ Player, Board }
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.util.Factories.BoardFactory.initRandomBoard
import de.htwg.se.gladiators.model.{ Gladiator, Shop }
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.model.Moves.movementType
import de.htwg.se.gladiators.util.MovementType
import java.util.concurrent.atomic.AtomicBoolean
import de.htwg.se.gladiators.model.TileType.{ Mine, Sand, Base }
import de.htwg.se.gladiators.util.Configuration
import de.htwg.se.gladiators.model.TileType

case class Controller(configuration: Configuration) extends ControllerInterface {
    val uncheckedStateMessage = "This code should not be reachable"
    var playerOne: Option[Player] = None
    var playerTwo: Option[Player] = None
    var board: Board = initRandomBoard(dimensions = configuration.boardSize)
    var shop = ShopFactory.initRandomShop(numberOfItemsInStock = configuration.itemsInShop)
    val shouldShutdown = new AtomicBoolean(false)

    implicit class Publish(notification: Events) {
        def broadcast = {
            publish(notification)
            notification
        }
    }

    implicit class MoveGladiators(gladiators: Vector[Gladiator]) {
        def move(from: Coordinate, to: Coordinate) = gladiators.map(gladiator => gladiator.position == from match {
            case true => gladiator.move(to)
            case false => gladiator
        })
        def attacked(points: Int, position: Coordinate) = gladiators.map(gladiator => gladiator.position == position match {
            case true => gladiator.attacked(points)
            case false => gladiator
        }).filter(_.healthPoints > 0)
    }

    override def inputCommand(command: Command): Events = {
        command match {
            case NamePlayerOne(name) => namePlayerOne(name)
            case NamePlayerTwo(name) => namePlayerTwo(name)
            case EndTurn => endTurn
            case BuyUnit(number, position) => buyUnit(number, position)
            case Move(from, to) => move(from, to)
            case Quit => {
                shouldShutdown.set(true)
                Shutdown.broadcast
            }
        }
    }

    def move(from: Coordinate, to: Coordinate): Events = {
        (gameState match {
            case TurnPlayerOne | TurnPlayerTwo => movementType(from, to, board, currentPlayer.get, enemyPlayer.get) match {
                case MovementType.Move => {
                    var (player, event) = updatePlayerMove(currentPlayer.get, from, to)
                    updateCurrentPlayer(Some(player))
                    event
                }
                case MovementType.Attack => attackUnit(from, to)
                case MovementType.BaseAttack => baseAttack(from)
                case MovementType.Mining => mining(to)
                case movementType: MovementType => ErrorMessage(movementType.message)
            }
            case _ => ErrorMessage(f"Cannot move in gameState $gameState")
        }).broadcast
    }

    def mining(position: Coordinate): Events = {
        val mine = board.tileAtCoordinate(position).asInstanceOf[Mine]
        mine.mine match {
            case Some(Mine(gold)) => {
                board = board.updateTile(position, Mine(gold))
                Mined(currentPlayer.get, mine.goldPerHit, false)
            }
            case None => {
                board = board.updateTile(position, Sand)
                Mined(currentPlayer.get, mine.gold, true)
            }
        }
    }

    def baseAttack(from: Coordinate): Events = {
        updateEnemyPlayer(Some(
            enemyPlayer.get.copy(
                health = (enemyPlayer.get.health - currentPlayer.get.gladiators.filter(_.position == from).head.attackPoints))))
        enemyPlayer.get.health <= 0 match {
            case true => Won(currentPlayer.get)
            case false => BaseAttacked(currentPlayer.get)
        }
    }

    def attackUnit(from: Coordinate, to: Coordinate): Events = {
        val numberOfGladiators = enemyPlayer.get.gladiators.length
        updateEnemyPlayer(Some(enemyPlayer.get.copy(gladiators = enemyPlayer
            .get
            .gladiators
            .attacked(currentPlayer
                .get
                .gladiators
                .filter(_.position == from).head.attackPoints, to))))

        Attacked(currentPlayer.get, (numberOfGladiators < enemyPlayer.get.gladiators.length), from, to)
    }

    def updatePlayerMove(player: Player, from: Coordinate, to: Coordinate): (Player, Events) = {
        val updatedPlayer = player.copy(gladiators = player.gladiators.move(from, to))
        val message = Moved(player, from, to, updatedPlayer.gladiators.filter(_.position == to).head)
        (updatedPlayer, message)
    }

    def buyUnit(number: Int, position: Coordinate): Events = {
        (shop.buy(number), gameState, board.isCoordinateLegal(position)) match {
            case (Some((newShop, gladiator)), state, true) if state == TurnPlayerOne || state == TurnPlayerTwo => {
                if (currentPlayer.get.alreadyBought == true)
                    ErrorMessage(f"You can just buy one unit per turn").broadcast
                else if (currentPlayer.get.placementTilesNewUnit(board.tiles.size, board.tiles).contains(position)) {
                    val (player, event) = checkoutFromShop(currentPlayer.get, newShop, gladiator.copy(position = position))
                    updateCurrentPlayer(Some(player))
                    return event.broadcast
                } else
                    return ErrorMessage(f"The position $position is blocked").broadcast
            }
            case (_, _, false) => ErrorMessage(f"You can not place a unit at $position").broadcast
            case (Some(_), _, _) => ErrorMessage(f"Cannot buy units in state $gameState").broadcast
            case (None, _, _) => ErrorMessage(f"Error buying from shop").broadcast
        }
    }

    def checkoutFromShop(player: Player, newShop: Shop, gladiator: Gladiator): (Player, Events) = {
        (player.credits - gladiator.calculateCost) match {
            case balance if balance >= 0 => {
                shop = newShop
                val newPlayer = player.copy(gladiators = player.gladiators :+ gladiator, credits = balance, alreadyBought = true)
                (newPlayer, SuccessfullyBoughtGladiator(newPlayer, gladiator))
            }
            case balance => {
                (player, ErrorMessage(f"You are ${balance * (-1)} credits short."))
            }
        }
    }

    @throws(classOf[Exception])
    def updateCurrentPlayer(player: Option[Player]) = gameState match {
        case TurnPlayerOne => playerOne = player
        case TurnPlayerTwo => playerTwo = player
        case _ => throw new Exception(uncheckedStateMessage)
    }

    @throws(classOf[Exception])
    def updateEnemyPlayer(player: Option[Player]) = gameState match {
        case TurnPlayerOne => playerTwo = player
        case TurnPlayerTwo => playerOne = player
        case _ => throw new Exception(uncheckedStateMessage)
    }

    def currentPlayer = gameState match {
        case TurnPlayerOne => playerOne
        case TurnPlayerTwo => playerTwo
        case _ => None
    }

    def enemyPlayer = gameState match {
        case TurnPlayerOne => playerTwo
        case TurnPlayerTwo => playerOne
        case _ => None
    }

    def resetGladiatorsMoved(player: Player) = Some(player.copy(gladiators = player.gladiators.map(_.copy(moved = false))))

    def endTurn: Events = {
        gameState match {
            case TurnPlayerOne => {
                gameState = TurnPlayerTwo
                playerTwo = resetGladiatorsMoved(playerTwo.get)
                playerTwo = Some(playerTwo.get.copy(alreadyBought = false))
                Turn(playerTwo.get).broadcast
            }
            case TurnPlayerTwo => {
                gameState = TurnPlayerOne
                playerOne = resetGladiatorsMoved(playerOne.get)
                playerOne = Some(playerOne.get.copy(alreadyBought = false))
                Turn(playerOne.get).broadcast
            }
            case _ => ErrorMessage(s"You can not end a turn in gameState $gameState").broadcast
        }
    }

    def namePlayerOne(name: String): Events = {
        // TODO: Player API lookup to set score
        gameState match {
            case NamingPlayerOne => {
                gameState = NamingPlayerTwo
                playerOne = Some(Player(name, board.tiles.size - 1, 100, 100, false))
                PlayerOneNamed(name).broadcast
            }
            case _ =>
                ErrorMessage(s"Player two can not be named anymore").broadcast
        }
    }

    def namePlayerTwo(name: String): Events = {
        // TODO: Player API lookup to set sc
        gameState match {
            case NamingPlayerTwo => {
                gameState = TurnPlayerOne
                playerTwo = Some(Player(name, 0, 100, 100, false))
                PlayerTwoNamed(name).broadcast
                Turn(playerOne.get).broadcast
            }
            case _ => ErrorMessage(s"Player two can not be named anymore").broadcast
        }
    }

    def tileOccupiedByCurrentPlayer(tile: Coordinate): Boolean = currentPlayer match {
        case None => false
        case Some(player) => !player.gladiators.filter(_.position == tile).isEmpty
    }

    def attackTiles(tile: Coordinate): Option[Vector[Coordinate]] = currentPlayer match {
        case None => None
        case Some(player) => player
            .gladiators
            .filter(_.position == tile)
            .map(gladiator => board.getValidCoordinates(gladiator.position, gladiator.gladiatorType.movementPointsAttack, Vector(Sand, Base)))
            .flatten match {
                case list: Vector[Coordinate] if list.length > 0 => Some(list)
                case _ => None
            }
    }

    def moveTiles(tile: Coordinate): Option[Vector[Coordinate]] = currentPlayer match {
        case None => None
        case Some(player) => player
            .gladiators
            .filter(_.position == tile)
            .map(gladiator => board.getValidCoordinates(gladiator.position, gladiator.movementPoints, Vector(Sand)))
            .flatten match {
                case list: Vector[Coordinate] if list.length > 0 => Some(list)
                case _ => None
            }
    }

    def newUnitPlacementTiles: Option[Vector[Coordinate]] = currentPlayer match {
        case None => None
        case Some(player) => Some(player.placementTilesNewUnit(board.tiles.length, board.tiles))
    }

    def boardTiles: Vector[Vector[TileType]] = board.tiles

    def gladiatorsPlayerOne: Option[Vector[Gladiator]] = playerOne match {
        case Some(player) => Some(player.gladiators)
        case None => None
    }

    def gladiatorsPlayerTwo: Option[Vector[Gladiator]] = playerTwo match {
        case Some(player) => Some(player.gladiators)
        case None => None
    }

    def stock: Vector[Gladiator] = shop.stock

    override def boardToString = board.coloredString(playerOne.get.gladiators ++ playerTwo.get.gladiators)

    override def shopToString = shop.toString
}
