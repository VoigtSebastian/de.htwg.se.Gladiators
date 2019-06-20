package de.htwg.se.gladiators.controller

object MoveType extends Enumeration {
    type MoveType = Value
    val LEGAL_MOVE, ILLEGAL_MOVE, ATTACK, BLOCKED, MOVE_TO_PALM, UNIT_NOT_OWNED_BY_PLAYER, INSUFFICIENT_MOVEMENT_POINTS, UNIT_NOT_EXISTING, BASE_ATTACK = Value

    val map = Map[MoveType, String](
        LEGAL_MOVE -> "This move is legal",
        ILLEGAL_MOVE -> "This move is our of bounds",
        ATTACK -> "This move represents an attack",
        BLOCKED -> "This tile is blocked by one of your own units",
        MOVE_TO_PALM -> "You can not move a unit to a palm cell",
        UNIT_NOT_OWNED_BY_PLAYER -> "The unit you are trying to move/attack is not owned by you",
        INSUFFICIENT_MOVEMENT_POINTS -> "The unit you are trying to move does not have enough movementPoints to move this far",
        UNIT_NOT_EXISTING -> "There is no unit at this coordinate",
        BASE_ATTACK -> "This move represents a base attack"
    )


    def message(moveType: MoveType): String = {
        map(moveType)
    }
}
