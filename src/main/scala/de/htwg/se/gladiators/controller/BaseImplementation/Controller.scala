package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.{ Player, Board }

case class Controller(playingFieldSize: Int) extends ControllerInterface {
    var playerOne: Option[Player] = None
    var playerTwo: Option[Player] = None
    var board: Option[Board] = None

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
            case BuyUnit(number) => println(s"Buying unit $number")
            case Move(from, to) => println(s"Moving from $from to $to")
            case Quit => println("Goodbye")
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
        playerOne = Some(Player(name, playingFieldSize - 1))
        publish(PlayerOneNamed(name))
    }

    def namePlayerTwo(name: String) = {
        // todo: Player API lookup to set sc
        gameState = TurnPlayerOne
        playerTwo = Some(Player(name, 0))
        publish(PlayerTwoNamed(name))
    }
}
