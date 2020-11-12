package de.htwg.se.gladiators.util

import de.htwg.se.gladiators.util.json.CoordinateJson._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class CoordinateSpec extends AnyWordSpec with Matchers {
    "A Coordinate" when {
        "created" should {
            "not fail" in {
                val coordinate = Coordinate(0, 0)
                coordinate.x should be(0)
                coordinate.y should be(0)
            }
            "have a String representation" in {
                Coordinate(0, 0).toString should not be (empty)
            }
            "have a json representation" in {
                Json.toJson(Coordinate(0, 0)).toString should not be (empty)
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
                (c1.equals(c2)) should be(false)
            }
            "be false because they are different classes" in {
                val c = Coordinate(0, 0)
                c.equals(1) should be(false)
            }
        }
        "checked for legality" should {
            "return true" in {
                Coordinate(0, 0).isLegal(1) should be(true)
            }
            "return false" in {
                Coordinate(1, 0).isLegal(1) should be(false)
            }
        }
    }
}
