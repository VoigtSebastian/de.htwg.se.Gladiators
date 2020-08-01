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
    var Board: Option[Board] = None

    override def inputCommand(command: Command): Unit = {
        command match {
            case NamePlayerOne(name) => {
                // todo: Player API lookup to set score
                namePlayerOne(name)
            }
            case NamePlayerTwo(name) => {
                // todo: Player API lookup to set score
                namePlayerTwo(name)
            }
            case EndTurn => gameState match {
                case TurnPlayerOne => publish(Turn(playerOne.get))
                case TurnPlayerTwo => publish(Turn(playerTwo.get))
                case _ => publish(ErrorMessage(s"You can not end a turn in gameState $gameState"))
            }
            case Attack(from, to) => println(s"Attacking from $from to $to")
            case BuyUnit(number) => println(s"Buying unit $number")
            case Move(from, to) => println(s"Moving from $from to $to")
            case Quit => println("Goodbye")
        }
    }

    def namePlayerOne(name: String) = {
        gameState = NamingPlayerTwo
        playerOne = Some(Player(name, playingFieldSize - 1))
        publish(PlayerOneNamed(name))
    }

    def namePlayerTwo(name: String) = {
        gameState = TurnPlayerOne
        playerTwo = Some(Player(name, 0))
        publish(PlayerTwoNamed(name))
    }
}
