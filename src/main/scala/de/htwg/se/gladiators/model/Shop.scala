package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Factories.GladiatorFactory

case class Shop(stock: Vector[Gladiator], itemsInStock: Int) {
    def buy(number: Int): Option[Shop] = {
        if (number < 1 || number > stock.length) return None
        Some(this.copy(stock = stock.updated(number - 1, GladiatorFactory.initRandomGladiator)))
    }
    override def toString: String = ???
}
