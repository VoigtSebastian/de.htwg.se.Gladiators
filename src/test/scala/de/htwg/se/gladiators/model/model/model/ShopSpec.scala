package de.htwg.se.gladiators.model.model.model

import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.gladiators.model.Shop
import de.htwg.se.gladiators.model.Player

class ShopSpec extends WordSpec with Matchers {
    "A Shop" when {
        "created " should {
            "have 10 Gladiators in stock " in {
                new Shop(10).stock.size should be (10)
            }
            "have a string representation " in {
                println(new Shop(10).toString())
            }
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
