package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.Player

case class Controller() extends ControllerInterface {

    override def inputCommand(command: Command): Unit = {
        command match {
            case NamePlayerOne(name) => {
                // todo: Player API lookup to set score
                gameState = NamingPlayerTwo
                publish(PlayerOneNamed(name))
            }
            case NamePlayerTwo(name) => {
                // todo: Player API lookup to set score
                gameState = TurnPlayerOne
                publish(PlayerTwoNamed(name))
            }
            case EndTurn => gameState match {
                case TurnPlayerOne => publish(Turn(Player("two")))
                case TurnPlayerTwo => publish(Turn(Player("one")))
                case _ => publish(ErrorMessage(s"You can not end a turn in gameState $gameState"))
            }
            case Attack(from, to) => println(s"Attacking from $from to $to")
            case BuyUnit(number) => println(s"Buying unit $number")
            case Move(from, to) => println(s"Moving from $from to $to")
            case Quit => println("Goodbye")
        }
    }
}
