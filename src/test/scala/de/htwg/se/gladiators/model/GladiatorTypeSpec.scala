package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.ShopFactory

class GladiatorTypeSpec extends AnyWordSpec with Matchers {
    val shop = ShopFactory.initRandomShop()
    "A GladiatorType" when {
        "used to represent stats" should {
            "have attack movement-points" in {
                GladiatorType.Archer.movementPointsAttack should be > 0
                GladiatorType.Knight.movementPointsAttack should be > 0
                GladiatorType.Tank.movementPointsAttack should be > 0
            }
        }
    }
    "Every GladiatorType" should {
        "have a string representation" in {
            GladiatorType
                .values
                .foreach(_.coloredString should not be (empty))
        }
        "have a short string representation" in {
            GladiatorType
                .values
                .foreach(_.shortString should not be (empty))
        }
        "have a simple string representation" in {
            GladiatorType
                .values
                .foreach(_.simpleString should not be (empty))
        }
    }
}
