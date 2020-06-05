package de.htwg.se.gladiators.model

import org.scalatest.{Matchers, WordSpec}

class ShopSpec extends WordSpec with Matchers {
    "A de.htwg.se.gladiators.model.Shop when " when {
        val shop = new Shop(10)
        "initialized with 10 Gladiators" in {
            shop.stock.size should be (10)
        }
        "used to buy gladiators" should {
            "return None" in {
                new Shop(10).buy(10, new Player()) should be (None)
                new Shop(10).buy(0, new Player(credits=0)) should be (None)
            }
            "return Some gladiator" in {
                new Shop(10).buy(0, new Player(credits=Int.MaxValue)) should not be empty
            }
            "restock with new gladiators, after a gladiator has been bought" in {
                var shop = Shop(10)
                val glad = shop.buy(0, new Player(credits=Int.MaxValue))
                glad should not be empty
                shop.stock.length should be (10)
                shop.stock foreach(item => item._1 != (glad.get) should be (true))
            }
            "increment the amount of turns after ending a turn" in {
                val shop = Shop(10)
                shop.endTurn()
                shop.kickOut(5)
                shop.stock foreach(item => item._2 should be (1))
            }
        }
    }
}
