package de.htwg.se.gladiators.model

import enumeratum._

sealed trait TileType extends EnumEntry {
    def coloredStringRepresentation(gladiator: Option[GladiatorType]): String
    def stringRepresentation(gladiator: Option[GladiatorType]): String
}

object TileType extends Enum[TileType] {
    val values = findValues

    case object Sand extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(220, gladiator, "S")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "S")
    }
    case object Palm extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(154, gladiator, "P")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "P")
    }
    case object Base extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(203, gladiator, "B")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "B")
    }
    case class Mine(gold: Int) extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(223, gladiator, "M")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "M")
        def goldPerHit = 10
        def mine = (gold - goldPerHit) match {
            case newValue if newValue > 0 => Some(this.copy(newValue))
            case _ => None
        }
    }

    def shortString(gladiator: Option[GladiatorType], letter: String) = gladiator match {
        case Some(gladiator) => gladiator.shortString
        case None => letter
    }
    def coloredString(color: Int, gladiator: Option[GladiatorType], letter: String) = s"\u001b[48;5;${color}m" + (gladiator match {
        case Some(gladiator) => gladiator.coloredString
        case None => s" $letter "
    }) + "\u001b[0m"
}
