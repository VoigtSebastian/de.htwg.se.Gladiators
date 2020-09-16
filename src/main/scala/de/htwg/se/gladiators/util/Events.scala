package de.htwg.se.gladiators.util

import scala.swing.event.Event
import enumeratum._
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.model.Gladiator

sealed trait Events extends EnumEntry with Event

object Events extends Enum[Events] {
    val values = findValues

    case object Init extends Events
    case class PlayerOneNamed(name: String) extends Events
    case class PlayerTwoNamed(name: String) extends Events
    case class SuccessfullyBoughtGladiator(player: Player, gladiator: Gladiator) extends Events
    case class Turn(player: Player) extends Events
    case class Moved(player: Player, from: Coordinate, to: Coordinate, gladiator: Gladiator) extends Events
    case class ErrorMessage(message: String) extends Events
}
