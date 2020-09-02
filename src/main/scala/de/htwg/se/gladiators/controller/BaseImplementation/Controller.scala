package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.{ Player, Board }
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.util.Factories.BoardFactory.initRandomBoard

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
            case Attack(from, to) => println(s"Attacking from $from to $to")
            case BuyUnit(number) => buyUnit(number)
            case Move(from, to) => println(s"Moving from $from to $to")
            case Quit => println("Goodbye")
        }
    }

    def buyUnit(number: Int): Unit = {
        shop.buy(number) match {
            // todo: Check and reduce credits
            case Some((newShop, gladiator)) => {
                shop = newShop
                gameState match {
                    case TurnPlayerOne => {
                        playerOne = Some(playerOne.get.copy(gladiators = (playerOne.get.gladiators :+ gladiator)))
                        publish(SuccessfullyBoughtGladiator(playerOne.get, gladiator))
                    }
                    case TurnPlayerTwo => {
                        playerTwo = Some(playerOne.get.copy(gladiators = (playerTwo.get.gladiators :+ gladiator)))
                        publish(SuccessfullyBoughtGladiator(playerTwo.get, gladiator))
                    }
                    case _ => publish(ErrorMessage("You can not buy from the shop currently"))
                }
            }
            case None => publish(ErrorMessage("Error buying from the shop"))
        }
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
