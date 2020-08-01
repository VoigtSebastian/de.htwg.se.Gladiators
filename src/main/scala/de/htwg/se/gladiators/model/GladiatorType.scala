package de.htwg.se.gladiators.model

import enumeratum._

sealed trait GladiatorType extends EnumEntry {
    def movementPointsAttack(): Int
}

object GladiatorType extends Enum[GladiatorType] {
    val values = findValues

    case object Tank extends GladiatorType { def movementPointsAttack(): Int = 1 }
    case object Knight extends GladiatorType { def movementPointsAttack(): Int = 2 }
    case object Archer extends GladiatorType { def movementPointsAttack(): Int = 3 }
}
