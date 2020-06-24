package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ShopSpec extends AnyWordSpec with Matchers {
    "A de.htwg.se.gladiators.model.Shop " when {
        val shop = new Shop(10)
        "initialized with 10 Gladiators" in {
            shop.stock.size should be(10)
        }
        "asking for a string representation " in {
            shop.toString should startWith("Units available in the shop:")
            shop.toString should include("AttackPoints")
            shop.toString should include("MovementPoints")
            shop.toString should include("Health Points")
            shop.toString should include("Cost")
        }
    }
}
