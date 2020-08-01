package de.htwg.se.gladiators.model

import enumeratum._

sealed trait CellType extends EnumEntry

object CellType extends Enum[CellType] {
    val values = findValues

    case object Sand extends CellType
    case object Palm extends CellType
    case object Base extends CellType
    case class Mine(gold: Int) extends CellType
}
