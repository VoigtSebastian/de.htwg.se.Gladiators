package de.htwg.se.gladiators.controller

object MoveType extends Enumeration {
    type MoveType = Value
    val LEGAL_MOVE, ILLEGAL_MOVE, ATTACK, BLOCKED = Value
}
