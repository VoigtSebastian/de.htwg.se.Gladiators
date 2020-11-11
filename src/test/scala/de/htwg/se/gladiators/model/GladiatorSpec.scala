package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.GladiatorFactory
import de.htwg.se.gladiators.util.Factories.ShopFactory

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GladiatorSpec extends AnyWordSpec with Matchers {
    val shop = ShopFactory.initRandomShop()
    "A Gladiator" when {
        "created" should {
            "have a cost" in {
                GladiatorFactory
                    .createGladiator()
                    .calculateCost should be > 0
            }
            "be movable" in {
                GladiatorFactory
                    .createGladiator(position = Some(Coordinate(0, 0)))
                    .move(Coordinate(1, 1))
                    .position should be(Coordinate(1, 1))
            }
            "reduce their health points when attacked by the correct amount" in {
                GladiatorFactory
                    .createGladiator(healthPoints = Some(10))
                    .attacked(10)
                    .healthPoints should be(0)
                GladiatorFactory
                    .createGladiator(healthPoints = Some(100))
                    .attacked(10)
                    .healthPoints should be(90)
                GladiatorFactory
                    .createGladiator(healthPoints = Some(0))
                    .attacked(10)
                    .healthPoints should be(-10)
            }
        }
    }
}
