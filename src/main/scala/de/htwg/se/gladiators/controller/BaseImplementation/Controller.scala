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

case class Controller() extends ControllerInterface {
    val uncheckedStateMessage = "This code should not be reachable"
    var playerOne: Option[Player] = None
    var playerTwo: Option[Player] = None
    var board: Board = initRandomBoard()
    var shop = ShopFactory.initRandomShop()

    implicit class Publish(notification: Events) {
        def broadcast = {
            publish(notification)
            notification
        }
    }

    implicit class MoveGladiator(gladiators: Vector[Gladiator]) {
        def move(from: Coordinate, to: Coordinate) = gladiators.map(gladiator => gladiator.position == from match {
            case true => gladiator.move(to)
            case false => gladiator
        })
    }

    override def inputCommand(command: Command): Unit = {
        command match {
            case NamePlayerOne(name) => namePlayerOne(name)
            case NamePlayerTwo(name) => namePlayerTwo(name)
            case EndTurn => endTurn
            case BuyUnit(number, position) => buyUnit(number, position)
            case Move(from, to) => move(from, to)
            case Quit => Shutdown.broadcast
        }
    }

    def move(from: Coordinate, to: Coordinate): Events = {
        gameState match {
            case TurnPlayerOne | TurnPlayerTwo => movementType(from, to, board, currentPlayer.get, enemyPlayer.get) match {
                case MovementType.Move => {
                    var (player, event) = updatePlayerMove(currentPlayer.get, from, to)
                    updateCurrentPlayer(Some(player))
                    event
                }
                case MovementType.Attack => ???
                case MovementType.BaseAttack => ???
                case movementType: MovementType => ErrorMessage(movementType.message).broadcast
            }
            case _ => ErrorMessage(f"Cannot move in gameState $gameState").broadcast
        }
    }

    def updatePlayerMove(player: Player, from: Coordinate, to: Coordinate): (Player, Events) = {
        val updatedPlayer = player.copy(gladiators = player.gladiators.move(from, to))
        val message = Moved(player, from, to, updatedPlayer.gladiators.filter(_.position == to).head).broadcast
        (updatedPlayer, message)
    }

    def buyUnit(number: Int, position: Coordinate): Events = {
        (shop.buy(number), gameState, board.isCoordinateLegal(position)) match {
            case (Some((newShop, gladiator)), state, true) if state == TurnPlayerOne || state == TurnPlayerTwo => {
                if (currentPlayer.get.placementTilesNewUnit(board.tiles.size, board.tiles).contains(position)) {
                    val (player, event) = checkoutFromShop(currentPlayer.get, newShop, gladiator.copy(position = position))
                    updateCurrentPlayer(Some(player))
                    return event
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
                val newPlayer = player.copy(gladiators = player.gladiators :+ gladiator, credits = balance)
                (newPlayer, SuccessfullyBoughtGladiator(newPlayer, gladiator).broadcast)
            }
            case balance => {
                (player, ErrorMessage(f"You are ${balance * (-1)} credits short.").broadcast)
            }
        }
    }

    @throws(classOf[Exception])
    def updateCurrentPlayer(player: Option[Player]) = gameState match {
        case TurnPlayerOne => playerOne = player
        case TurnPlayerTwo => playerTwo = player
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
                Turn(playerTwo.get).broadcast
            }
            case TurnPlayerTwo => {
                gameState = TurnPlayerOne
                playerOne = resetGladiatorsMoved(playerOne.get)
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
                playerOne = Some(Player(name, board.tiles.size - 1, 100))
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
                playerTwo = Some(Player(name, 0, 100))
                PlayerTwoNamed(name).broadcast
                Turn(playerOne.get).broadcast
            }
            case _ => ErrorMessage(s"Player two can not be named anymore").broadcast
        }
    }

    override def boardToString = board.coloredString(playerOne.get.gladiators ++ playerTwo.get.gladiators)

    override def shopToString = shop.toString
}
