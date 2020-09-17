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
    var playerOne: Option[Player] = None
    var playerTwo: Option[Player] = None
    var board: Board = initRandomBoard()
    var shop = ShopFactory.initRandomShop()

    implicit class Publish(notification: Events) {
        def broadcast = publish(notification)
    }

    implicit class MoveGladiator(gladiators: Vector[Gladiator]) {
        def move(from: Coordinate, to: Coordinate) = gladiators.map(gladiator => gladiator.position == from match {
            case true => gladiator.move(to)
            case false => gladiator
        })
    }

    override def inputCommand(command: Command): Unit = {
        command match {
            case NamePlayerOne(name) => {
                if (gameState == NamingPlayerOne) namePlayerOne(name)
                else ErrorMessage(s"Player one can not be named anymore").broadcast
            }
            case NamePlayerTwo(name) => {
                if (gameState == NamingPlayerTwo) namePlayerTwo(name)
                else ErrorMessage(s"Player two can not be named anymore").broadcast
            }
            case EndTurn => endTurn
            case BuyUnit(number, position) => buyUnit(number, position)
            case Move(from, to) if gameState == TurnPlayerOne || gameState == TurnPlayerTwo => move(from, to)
            case Move(_, _) => ErrorMessage(f"Cannot move in gameState $gameState").broadcast
            case Quit => Shutdown.broadcast
        }
    }

    def move(from: Coordinate, to: Coordinate): Unit = {
        movementType(from, to, board, currentPlayer.get, enemyPlayer.get) match {
            case MovementType.Move => {
                gameState match {
                    case TurnPlayerOne => playerOne = Some(updatePlayerMove(playerOne.get, from, to))
                    case TurnPlayerTwo => playerTwo = Some(updatePlayerMove(playerTwo.get, from, to))
                    case _ => ErrorMessage("Internal Controller error").broadcast
                }
            }
            case MovementType.Attack => ???
            case MovementType.AlreadyMoved => ???
            case movementType: MovementType => ErrorMessage(movementType.message).broadcast
        }
    }

    def updatePlayerMove(player: Player, from: Coordinate, to: Coordinate): Player = {
        val updatedPlayer = player.copy(gladiators = player.gladiators.move(from, to))
        Moved(player, from, to, updatedPlayer.gladiators.filter(_.position == to).head).broadcast
        updatedPlayer
    }

    def buyUnit(number: Int, position: Coordinate): Unit = {
        (shop.buy(number), gameState, board.isCoordinateLegal(position)) match {
            case (Some((newShop, gladiator)), TurnPlayerOne, true) => {
                if (playerOne.get.placementTilesNewUnit(board.tiles.size, board.tiles).contains(position))
                    playerOne = Some(checkoutFromShop(playerOne.get, newShop, gladiator.copy(position = position)))
                else
                    ErrorMessage(f"You can not place a unit at this position").broadcast
            }
            case (Some((newShop, gladiator)), TurnPlayerTwo, true) => {
                if (playerTwo.get.placementTilesNewUnit(board.tiles.size, board.tiles).contains(position))
                    playerTwo = Some(checkoutFromShop(playerTwo.get, newShop, gladiator.copy(position = position)))
                else
                    ErrorMessage(f"You can not place a unit at this position").broadcast
            }
            case (_, _, false) => ErrorMessage(f"You can not place a unit at $position")
            case (Some(_), _, _) => ErrorMessage(f"Cannot buy units in state $gameState").broadcast
            case (None, _, _) => ErrorMessage(f"Error buying from shop").broadcast
        }
    }

    def checkoutFromShop(player: Player, newShop: Shop, gladiator: Gladiator): Player = {
        (player.credits - gladiator.calculateCost) match {
            case balance if balance >= 0 => {
                shop = newShop
                val newPlayer = player.copy(gladiators = player.gladiators :+ gladiator, credits = balance)
                SuccessfullyBoughtGladiator(newPlayer, gladiator).broadcast
                newPlayer
            }
            case balance => {
                ErrorMessage(f"You are ${balance * (-1)} credits short.").broadcast
                player
            }
        }
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

    def endTurn = {
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

    def namePlayerOne(name: String) = {
        // todo: Player API lookup to set score
        gameState = NamingPlayerTwo
        playerOne = Some(Player(name, board.tiles.size - 1, 100))
        PlayerOneNamed(name).broadcast
    }

    def namePlayerTwo(name: String) = {
        // todo: Player API lookup to set sc
        gameState = TurnPlayerOne
        playerTwo = Some(Player(name, 0, 100))
        PlayerTwoNamed(name).broadcast
        Turn(playerOne.get).broadcast
    }

    override def boardToString = board.coloredString(playerOne.get.gladiators ++ playerTwo.get.gladiators)

    override def shopToString = shop.toString
}
