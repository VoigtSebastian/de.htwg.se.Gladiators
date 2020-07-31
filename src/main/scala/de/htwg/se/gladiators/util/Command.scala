package de.htwg.se.gladiators.util

import enumeratum._

sealed trait Command extends EnumEntry

object Command extends Enum[Command] {
    val values = findValues

    case class Move(from: Coordinate, to: Coordinate) extends Command
    case class Attack(from: Coordinate, to: Coordinate) extends Command
    case class BuyUnit(number: Int) extends Command
    case object Quit extends Command

}
