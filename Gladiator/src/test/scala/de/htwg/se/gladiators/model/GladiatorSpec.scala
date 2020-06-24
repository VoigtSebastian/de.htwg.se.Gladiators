package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GladiatorSpec extends AnyWordSpec with Matchers {
    "A Gladiator is a fighter in the game. A Gladiator" when {

        "new" should {
            var gladiator = Gladiator(5, 1, 10.0, 50.0, 100.0, GladiatorType.SWORD, new Player)

            "have a x coordinate" in {
                gladiator.line should be(5)
            }
            "have a y coordinate" in {
                gladiator.row should be(1)
            }
            "have movementPoints" in {
                gladiator.movementPoints should be(10.0)
            }
            "have ap" in {
                gladiator.ap should be(50.0)
            }
            "have hp" in {
                gladiator.hp should be(100.0)
            }
            "have a type " in {
                gladiator.gladiatorType should be(GladiatorType.SWORD)
            }
        }
        "after move" should {
            var gladiator3 = Gladiator(5, 5, 10.0, 50.0, 100.0, GladiatorType.TANK, new Player)
            gladiator3 = gladiator3.move(4, 4)
            "have a new location" in {
                gladiator3.line should be(4)
                gladiator3.row should be(4)
            }

        }

    }
}
