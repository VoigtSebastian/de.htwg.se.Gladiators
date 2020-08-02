package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.ShopFactory

class ShopSpec extends AnyWordSpec with Matchers {
    val shop = ShopFactory.initRandomShop()
    "A Shop" when {
        "buying items" should {
            "return None" in {
                shop.buy(11) should be(None)
                shop.buy(0) should be(None)
                shop.buy(-1) should be(None)
            }
            "return a new Shop" in {
                shop.buy(2).isInstanceOf[Some[Shop]]
                shop.buy(1).isInstanceOf[Some[Shop]]
                shop.buy(10).isInstanceOf[Some[Shop]]
            }
            "replace the correct Gladiator" in {
                val glad = shop.stock(0)
                shop.buy(1).get.stock(0) should not be (glad)
            }
        }
    }
}
