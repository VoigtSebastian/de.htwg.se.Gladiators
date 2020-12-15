package de.htwg.se.gladiators.model.playerService

import enumeratum._

sealed trait PlayerServiceReturnTypes extends EnumEntry

object PlayerServiceReturnTypes extends Enum[PlayerServiceReturnTypes] {
    val values = findValues

    case class Player(
        id: Int,
        player_name: String,
        games_played: Int,
        games_won: Int) extends PlayerServiceReturnTypes
    case class Error(
        error_type: String,
        message: String)
}
