package de.htwg.se.gladiators.model

import enumeratum._
import play.api.libs.json.JsObject
import play.api.libs.json.Json

sealed trait TileType extends EnumEntry {
    def coloredStringRepresentation(gladiator: Option[GladiatorType]): String
    def stringRepresentation(gladiator: Option[GladiatorType]): String
    def toJsObject: JsObject
}

object TileType extends Enum[TileType] {
    val values = findValues

    case object Sand extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(220, gladiator, "S")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "S")
        override def toJsObject: JsObject = Json.obj(
            "type" -> "Sand")
    }
    case object Palm extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(154, gladiator, "P")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "P")
        override def toJsObject: JsObject = Json.obj(
            "type" -> "Palm")
    }
    case object Base extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(203, gladiator, "B")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "B")
        override def toJsObject: JsObject = Json.obj(
            "type" -> "Base")
    }
    case class Mine(gold: Int) extends TileType {
        override def coloredStringRepresentation(gladiator: Option[GladiatorType]): String = coloredString(223, gladiator, "M")
        override def stringRepresentation(gladiator: Option[GladiatorType]): String = shortString(gladiator, "M")
        override def toJsObject: JsObject = Json.obj(
            "type" -> "Mine",
            "gold" -> gold)
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
