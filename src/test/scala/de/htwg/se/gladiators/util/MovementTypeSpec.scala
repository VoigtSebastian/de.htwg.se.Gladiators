package de.htwg.se.gladiators.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MovementTypeSpec extends AnyWordSpec with Matchers {
    "Every MovementType" when {
        "created" should {
            "have a string representation" in {
                MovementType
                    .values
                    .foreach(_.message should not be (empty))
            }
        }
    }
}
