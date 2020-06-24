package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ShopSpec extends AnyWordSpec with Matchers {
    "A de.htwg.se.gladiators.model.Shop when " when {
        val shop = new Shop(10)
        "initialized with 10 Gladiators" in {
            shop.stock.size should be (10)
        }
        "have a nice string representation " in {
            println(shop.toString)
        }
    }
}
