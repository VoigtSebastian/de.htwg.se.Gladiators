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
            "have a string representation" in {
                GladiatorType.Archer.coloredString should contain('A')
                GladiatorType.Knight.coloredString should contain('K')
                GladiatorType.Tank.coloredString should contain('T')
            }
        }
    }
}
