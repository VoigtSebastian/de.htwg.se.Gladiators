package de.htwg.se.gladiators.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.GladiatorFactory
import de.htwg.se.gladiators.model.Gladiator

class GladiatorFactorySpec extends AnyWordSpec with Matchers {
    "A GladiatorFactory" when {
        "used to create a random Gladiator" should {
            "return useful gladiators" in {
                GladiatorFactory.initRandomGladiator.isInstanceOf[Gladiator] should be(true)
            }
        }
        "creating random GladiatorTypes" should {
            "not fail" in {
                (0 to 500)
                    .map(_ => GladiatorFactory.createType)
                    .distinct
                    .length should be(3)
            }
        }
        "creating random numbers" should {
            "return them in a chosen interval" in {
                for (_ <- 0 to 500) {
                    val number = GladiatorFactory.randomNumberInterval(0, 10)
                    (number >= 0 && number <= 10) should be(true)
                }
            }
        }
    }
}
