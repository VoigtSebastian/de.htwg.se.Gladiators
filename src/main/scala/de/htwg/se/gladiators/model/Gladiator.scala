package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate

case class Gladiator(
    gladiatorType: GladiatorType,
    position: Coordinate,
    healthPoints: Int,
    movementPoints: Int,
    attackPoints: Int,
    moved: Boolean)(
        healthOnInitialization: Int = healthPoints,
        initialCost: Int = (((attackPoints + healthPoints) * (movementPoints + 1)) / 35).toInt) {

    // TODO: Test this
    def copy(gladiatorType: GladiatorType = gladiatorType,
        position: Coordinate = position,
        healthPoints: Int = healthPoints,
        movementPoints: Int = movementPoints,
        attackPoints: Int = attackPoints,
        moved: Boolean = moved): Gladiator = Gladiator(
            gladiatorType,
            position,
            healthPoints,
            movementPoints,
            attackPoints,
            moved
        )(this.initialHealth, this.initialCost)

    def initialHealth = healthOnInitialization
    def cost = initialCost

    def move(to: Coordinate) = this.copy(position = to, moved = true)
    def attacked(points: Int) = this.copy(healthPoints = (healthPoints - points))
}
