package de.htwg.se.gladiators.controller.controllerComponent

object MoveType extends Enumeration {
    type MoveType = Value
    val LEGAL_MOVE,
    ILLEGAL_MOVE,
    ATTACK,
    BLOCKED,
    MOVE_TO_PALM,
    UNIT_NOT_OWNED_BY_PLAYER,
    INSUFFICIENT_MOVEMENT_POINTS,
    UNIT_NOT_EXISTING,
    GOLD,
    BASE_ATTACK,
    ALREADY_MOVED,
    MOVE_OUT_OF_BOUNDS,
    OWN_BASE = Value

    val map: Map[MoveType, String] = Map[MoveType, String](
        LEGAL_MOVE -> "This move is legal",
        ILLEGAL_MOVE -> "This move is our of bounds",
        ATTACK -> "This move represents an attack",
        BLOCKED -> "This tile is blocked by one of your own units",
        MOVE_TO_PALM -> "You can not move a unit to a palm cell",
        UNIT_NOT_OWNED_BY_PLAYER -> "The unit you are trying to move/attack is not owned by you",
        INSUFFICIENT_MOVEMENT_POINTS -> "The unit you are trying to move does not have enough movementPoints to move this far",
        UNIT_NOT_EXISTING -> "There is no unit at this coordinate",
        GOLD -> "Mining Gold",
        BASE_ATTACK -> "This move represents a base attack",
        ALREADY_MOVED -> "This Gladiator already moved",
        MOVE_OUT_OF_BOUNDS -> "This move is out of bounds",
        OWN_BASE -> "You can not attack your own base"
    )


    def message(moveType: MoveType): String = {
        map(moveType)
    }
}
