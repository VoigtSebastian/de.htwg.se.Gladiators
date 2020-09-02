package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.{ Player, Board }
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.util.Factories.BoardFactory.initRandomBoard
import de.htwg.se.gladiators.model.{ Gladiator, Shop }

case class Controller(playingFieldSize: Int) extends ControllerInterface {
    var playerOne: Option[Player] = None
    var playerTwo: Option[Player] = None
    var board: Option[Board] = Some(initRandomBoard())
    var shop = ShopFactory.initRandomShop()

    override def inputCommand(command: Command): Unit = {
        command match {
            case NamePlayerOne(name) => {
                if (gameState == NamingPlayerOne) namePlayerOne(name)
                else publish(ErrorMessage(s"Player one can not be named anymore"))
            }
            case NamePlayerTwo(name) => {
                if (gameState == NamingPlayerTwo) namePlayerTwo(name)
                else publish(ErrorMessage(s"Player two can not be named anymore"))
            }
            case EndTurn => endTurn
            case BuyUnit(number) => buyUnit(number)
            case Move(from, to) => println(s"Moving from $from to $to")
            case Quit => println("Goodbye")
        }
    }

    def buyUnit(number: Int): Unit = {
        (shop.buy(number), gameState) match {
            case (Some((newShop, gladiator)), TurnPlayerOne) => playerOne = Some(checkoutFromShop(playerOne.get, newShop, gladiator))
            case (Some((newShop, gladiator)), TurnPlayerTwo) => playerTwo = Some(checkoutFromShop(playerTwo.get, newShop, gladiator))
            case (Some(_), _) => publish(ErrorMessage(f"Cannot buy units in state $gameState"))
            case (None, _) => publish(ErrorMessage(f"Error buying from shop"))
        }
    }

    def checkoutFromShop(player: Player, newShop: Shop, gladiator: Gladiator): Player = {
        (player.credits - gladiator.calculateCost) match {
            case balance if balance >= 0 => {
                shop = newShop
                val newPlayer = player.copy(gladiators = player.gladiators :+ gladiator, credits = balance)
                publish(SuccessfullyBoughtGladiator(newPlayer, gladiator))
                newPlayer
            }
            case balance => {
                publish(ErrorMessage(f"You are ${balance * (-1)} credits short."))
                player
            }
        }
    }

    def currentPlayer = gameState match {
        case TurnPlayerOne => playerOne.get
        case TurnPlayerTwo => playerTwo.get
        case _ => None
    }

    def endTurn = {
        gameState match {
            case TurnPlayerOne => {
                gameState = TurnPlayerTwo
                publish(Turn(playerTwo.get))
            }
            case TurnPlayerTwo => {
                gameState = TurnPlayerOne
                publish(Turn(playerOne.get))
            }
            case _ => publish(ErrorMessage(s"You can not end a turn in gameState $gameState"))
        }
    }

    def namePlayerOne(name: String) = {
        // todo: Player API lookup to set score
        gameState = NamingPlayerTwo
        playerOne = Some(Player(name, 100, playingFieldSize - 1))
        publish(PlayerOneNamed(name))
    }

    def namePlayerTwo(name: String) = {
        // todo: Player API lookup to set sc
        gameState = TurnPlayerOne
        playerTwo = Some(Player(name, 100, 0))
        publish(PlayerTwoNamed(name))
        publish(Turn(playerOne.get))
    }

    override def boardToString = board match {
        case None => {
            publish(ErrorMessage("No board initialized"))
            ""
        }
        case Some(board) => board.coloredString(playerOne.get.gladiators ++ playerTwo.get.gladiators)
    }
}
