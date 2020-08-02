package de.htwg.se.gladiators.util.Factories

import de.htwg.se.gladiators.model.Shop

object ShopFactory {
    def initRandomShop(numberOfItemsInStock: Int = 10) = Shop(
        stock = (1 to numberOfItemsInStock)
            .map(_ => GladiatorFactory.initRandomGladiator)
            .toVector,
        numberOfItemsInStock)
}
