package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate

case class Gladiator(
    gladiatorType: GladiatorType,
    position: Coordinate,
    healthPoints: Int,
    movementPoints: Int,
    attackPoints: Int,
    moved: Boolean) {
    def calculateCost = ((healthPoints * 2) + (movementPoints * 3) + (attackPoints * 2))
}
