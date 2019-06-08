package de.htwg.se.gladiators.controller

object MoveType extends Enumeration {
    type MoveType = Value
    val LEGAL_MOVE, ILLEGAL_MOVE, ATTACK, BLOCKED = Value

    val map = Map[MoveType, String](
        LEGAL_MOVE -> "This move is legal",
        ILLEGAL_MOVE -> "This move is our of bounds",
        ATTACK -> "This move represents an attack",
        BLOCKED -> "This tile is blocked by one of your own units"
    )


    def message(moveType: MoveType): String = {
        map(moveType)
    }
}
