package de.htwg.se.gladiators.util

import scala.swing.event.Event
import enumeratum._
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.controller.ControllerInterface

sealed trait Events extends EnumEntry with Event

object Events extends Enum[Events] {
    val values = findValues

    case object Init extends Events
    case class PlayerOneNamed(newController: ControllerInterface, name: String) extends Events
    case class PlayerTwoNamed(newController: ControllerInterface, name: String) extends Events
    case class SuccessfullyBoughtGladiator(newController: ControllerInterface, player: Player, gladiator: Gladiator) extends Event
    case class Turn(player: Player) extends Events
    case class ErrorMessage(message: String) extends Events
}
