package de.htwg.se.gladiators.model

import org.scalatest.{Matchers, WordSpec}

class ShopSpec extends WordSpec with Matchers {
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
