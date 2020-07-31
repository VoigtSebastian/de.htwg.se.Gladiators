package de.htwg.se.gladiators.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ControllerSpec extends AnyWordSpec with Matchers {
    "A Coordinate" when {
        "created" should {
            "not fail" in {
                val coordinate = Coordinate(0, 0)
                coordinate.x should be(0)
                coordinate.y should be(0)
            }
        }
        "compared" should {
            "be the same " in {
                val c1 = Coordinate(0, 0)
                val c2 = Coordinate(0, 0)
                (c1 == c2) should be(true)
            }
            "be different" in {
                val c1 = Coordinate(0, 0)
                val c2 = Coordinate(0, 1)
                (c1 == c2) should be(false)
            }
        }
    }
}