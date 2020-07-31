package de.htwg.se.gladiators.controller

import enumeratum._

sealed trait GameState extends EnumEntry

object GameState extends Enum[GameState] {
    val values = findValues

    case object NamingPlayerOne extends GameState
    case object NamingPlayerTwo extends GameState
    case object TurnPlayerOne extends GameState
    case object TurnPlayerTwo extends GameState
    case object Finished extends GameState
}
