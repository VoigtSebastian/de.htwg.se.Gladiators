package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Factories.GladiatorFactory

case class Shop(stock: Vector[Gladiator], itemsInStock: Int) {
    def buy(number: Int): Option[Shop] = {
        if (number < 1 || number > stock.length) return None
        Some(this.copy(stock = stock.updated(number - 1, GladiatorFactory.initRandomGladiator)))
    }

    def formatLine(unitType: String, attackPoints: String, movementPoints: String, healthPoints: String, cost: String) = {
        f"$unitType%-10s| $attackPoints%-13s | $movementPoints%-15s | $healthPoints%-13s | $cost%-4s\n"
    }

    override def toString: String = (formatLine("Type", "Attack-Points", "Movement-Points", "Health-Points", "Cost") +
        stock.map(g => formatLine(g.gladiatorType.simpleString, g.attackPoints.toString, g.movementPoints.toString, g.healthPoints.toString, g.calculateCost.toString)).mkString)
}
