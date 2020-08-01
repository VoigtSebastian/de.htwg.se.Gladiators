package de.htwg.se.gladiators.util

import de.htwg.se.gladiators.model.Player

import scala.swing.event.Event
import enumeratum._

sealed trait Events extends EnumEntry with Event

object Events extends Enum[Events] {
    val values = findValues

    case object Init extends Event
    case class PlayerOneNamed(name: String) extends Events
    case class PlayerTwoNamed(name: String) extends Events
    case class Turn(player: Player) extends Event
    case class ErrorMessage(message: String) extends Event
}
