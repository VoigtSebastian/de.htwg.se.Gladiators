package de.htwg.se.gladiators.util

import scala.swing.event.Event

import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.model.Player

import enumeratum._

sealed trait Events extends EnumEntry with Event

object Events extends Enum[Events] {
    val values = findValues

    case object Init extends Events
    case class PlayerOneNamed(name: String) extends Events
    case class PlayerTwoNamed(name: String) extends Events
    case class SuccessfullyBoughtGladiator(player: Player, gladiator: Gladiator) extends Events
    case class Turn(player: Player) extends Events
    case class Moved(player: Player, from: Coordinate, to: Coordinate, gladiator: Gladiator) extends Events
    case class Attacked(currentPlayer: Player, killed: Boolean, from: Coordinate, to: Coordinate) extends Events
    case class BaseAttacked(currentPlayer: Player) extends Events
    case class Mined(currentPlayer: Player, amount: Int, depleted: Boolean) extends Events
    case object Shutdown extends Events
    case class Won(player: Player) extends Events
    case class ErrorMessage(message: String) extends Events
}
