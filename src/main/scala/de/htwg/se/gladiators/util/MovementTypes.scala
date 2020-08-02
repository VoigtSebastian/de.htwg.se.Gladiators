package de.htwg.se.gladiators.util

import enumeratum._

sealed trait MovementTypes extends EnumEntry

object MovementTypes extends Enum[MovementTypes] {
    val values = findValues

    case object MoveOutOfBounds extends MovementTypes
    case object Move extends MovementTypes
    case object Attack extends MovementTypes
    case object NotOwnedByPlayer extends MovementTypes
    case object TileBlocked extends MovementTypes
}
