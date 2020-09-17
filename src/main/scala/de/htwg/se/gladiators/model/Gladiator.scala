package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate

case class Gladiator(
    gladiatorType: GladiatorType,
    position: Coordinate,
    healthPoints: Int,
    movementPoints: Int,
    attackPoints: Int,
    moved: Boolean) {
    def calculateCost = (((attackPoints + healthPoints) * (movementPoints + 1)) / 35).toInt
    def move(to: Coordinate) = this.copy(position = to)
}
