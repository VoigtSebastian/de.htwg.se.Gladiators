package de.htwg.se.gladiators.model

import enumeratum._

sealed trait GladiatorType extends EnumEntry {
    def movementPointsAttack: Int
    def shortString: String
    def simpleString: String
    def coloredString: String
}

object GladiatorType extends Enum[GladiatorType] {
    val values = findValues

    case object Tank extends GladiatorType {
        override def movementPointsAttack(): Int = 1
        override def shortString: String = "T"
        override def simpleString: String = "Tank"
        override def coloredString = coloredStringHelper(21, "T")
    }
    case object Knight extends GladiatorType {
        override def movementPointsAttack(): Int = 1
        override def shortString: String = "K"
        override def simpleString: String = "Knight"
        override def coloredString = coloredStringHelper(196, "K")
    }
    case object Archer extends GladiatorType {
        override def movementPointsAttack(): Int = 2
        override def shortString: String = "A"
        override def simpleString: String = "Archer"
        override def coloredString = coloredStringHelper(210, "A")
    }

    def coloredStringHelper(color: Int, letter: String) = s"\u001b[38;5;${color}m ${letter} \u001b[0m"
}
