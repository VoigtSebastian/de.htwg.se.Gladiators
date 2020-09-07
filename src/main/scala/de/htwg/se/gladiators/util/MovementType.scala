package de.htwg.se.gladiators.util

import enumeratum._

sealed trait MovementType extends EnumEntry {
    def message: String
}

object MovementType extends Enum[MovementType] {
    val values = findValues

    case object Move extends MovementType {
        def message = ""
    }
    case object Attack extends MovementType {
        def message = ""
    }
    case object BaseAttack extends MovementType {
        def message = ""
    }
    case object MoveOutOfBounds extends MovementType {
        def message = "This move is out of bounds"
    }
    case object NotOwnedByPlayer extends MovementType {
        def message = "This unit is not owned by you"
    }
    case object NoUnitAtCoordinate extends MovementType {
        def message = "There is no unit at this coordinate"
    }
    case object TileBlocked extends MovementType {
        def message = "The tile you are trying to move to, is blocked by another unit"
    }
    case object MoveToPalm extends MovementType {
        def message = "You can not move onto Palm"
    }
    case object IllegalMove extends MovementType {
        def message = "This move is not possible"
    }
    case object AlreadyMoved extends MovementType {
        def message = "The unit you are trying to move, has already moved this turn"
    }
}
