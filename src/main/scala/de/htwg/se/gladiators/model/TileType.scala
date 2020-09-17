package de.htwg.se.gladiators.model

import enumeratum._

sealed trait TileType extends EnumEntry {
    def coloredStringRepresentation(gladiator: Option[GladiatorType]): String
}

object TileType extends Enum[TileType] {
    val values = findValues

    case object Sand extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(220, gladiator, "S")
    }
    case object Palm extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(154, gladiator, "P")
    }
    case object Base extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(203, gladiator, "B")
    }
    case class Mine(gold: Int) extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(223, gladiator, "M")
    }

    def coloredString(color: Int, gladiator: Option[GladiatorType], letter: String) = s"\u001b[48;5;${color}m" + (gladiator match {
        case Some(gladiator) => gladiator.coloredString
        case None => s" $letter "
    }) + "\u001b[0m"
}
