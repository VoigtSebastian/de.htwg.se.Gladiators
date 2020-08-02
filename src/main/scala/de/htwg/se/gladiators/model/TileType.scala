package de.htwg.se.gladiators.model

import enumeratum._

sealed trait TileType extends EnumEntry

object TileType extends Enum[TileType] {
    val values = findValues

    case object Sand extends TileType
    case object Palm extends TileType
    case object Base extends TileType
    case class Mine(gold: Int) extends TileType
}
