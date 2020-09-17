package de.htwg.se.gladiators.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MovementTypeSpec extends AnyWordSpec with Matchers {
    "Every MovementType" when {
        "created" should {
            "have a string representation" in {
                MovementType
                    .values
                    .foreach(_.toString should not be (empty))
            }
        }
    }
}
