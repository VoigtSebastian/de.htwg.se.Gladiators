package de.htwg.se.gladiators.model.model.model

import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.gladiators.model.Shop

class ShopSpec extends WordSpec with Matchers {
    "A Shop when " when {
        val shop = new Shop(10)
        "initialized with 10 Gladiators" in {
            shop.stock.size should be (10)
        }
        "have a nice string representation " in {
            println(shop.toString)
        }
    }
}
