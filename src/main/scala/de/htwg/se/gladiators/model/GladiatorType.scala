package de.htwg.se.gladiators.model

import enumeratum._

sealed trait GladiatorType extends EnumEntry {
    def movementPointsAttack(): Int
    def toString(): String
}

object GladiatorType extends Enum[GladiatorType] {
    val values = findValues

    case object Tank extends GladiatorType {
        override def movementPointsAttack(): Int = 1
        override def toString = coloredString(21, "T")
    }
    case object Knight extends GladiatorType {
        override def movementPointsAttack(): Int = 2
        override def toString = coloredString(196, "K")
    }
    case object Archer extends GladiatorType {
        override def movementPointsAttack(): Int = 3
        override def toString = coloredString(210, "A")
    }

    def coloredString(color: Int, letter: String) = s"\u001b[38;5;${color}m ${letter} \u001b[0m"
}
