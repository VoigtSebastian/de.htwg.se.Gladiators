package de.htwg.se.gladiators.util

import enumeratum._

sealed trait MovementType extends EnumEntry

object MovementType extends Enum[MovementType] {
    val values = findValues

    case object Move extends MovementType
    case object Attack extends MovementType
    case object BaseAttack extends MovementType
    case object MoveOutOfBounds extends MovementType
    case object NotOwnedByPlayer extends MovementType
    case object NoUnitAtCoordinate extends MovementType
    case object TileBlocked extends MovementType
    case object MoveToPalm extends MovementType
}
