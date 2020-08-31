package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.util.Factories.GladiatorFactory

class GladiatorSpec extends AnyWordSpec with Matchers {
    val shop = ShopFactory.initRandomShop()
    "A Gladiator" when {
        "created" should {
            "have a cost" in {
                GladiatorFactory.createGladiator().calculateCost should be > 0
            }
        }
    }
}
