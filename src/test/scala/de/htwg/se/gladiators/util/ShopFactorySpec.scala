package de.htwg.se.gladiators.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.ShopFactory

class ShopFactorySpec extends AnyWordSpec with Matchers {
    "A ShopFactory" when {
        "creating Shops" should {
            "have the correct number of items" in {
                ShopFactory.initRandomShop(0).stock.length should be(0)
                ShopFactory.initRandomShop(10).stock.length should be(10)
                ShopFactory.initRandomShop(3).stock.length should be(3)
            }
            "have the correct value set for items in stock" in {
                ShopFactory.initRandomShop(0).itemsInStock should be(0)
                ShopFactory.initRandomShop(10).itemsInStock should be(10)
                ShopFactory.initRandomShop(3).itemsInStock should be(3)
            }
        }
    }
}
