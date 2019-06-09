package de.htwg.se.gladiators.controller

object MoveType extends Enumeration {
    type MoveType = Value
    val LEGAL_MOVE, ILLEGAL_MOVE, ATTACK, BLOCKED, MOVE_TO_PALM, UNIT_NOT_OWNED_BY_PLAYER = Value

    val map = Map[MoveType, String](
        LEGAL_MOVE -> "This move is legal",
        ILLEGAL_MOVE -> "This move is our of bounds",
        ATTACK -> "This move represents an attack",
        BLOCKED -> "This tile is blocked by one of your own units",
        MOVE_TO_PALM -> "You can not move a unit to a palm cell",
        UNIT_NOT_OWNED_BY_PLAYER -> "The unit you are trying to move is not owned by you"
    )


    def message(moveType: MoveType): String = {
        map(moveType)
    }
}
