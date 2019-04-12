package de.htwg.se.gladiators.model.model

import de.htwg.se.gladiators.model.{Cell, Gladiator}
import de.htwg.se.gladiators.model.GladiatorType._
import org.scalatest.{Matchers, WordSpec}

class GladiatorSpec extends WordSpec with Matchers {
  "A Gladiator is a fighter in the game. A Gladiator" when {

    "new" should {
      var gladiator = Gladiator(Cell(0), 10.0, 50.0, 100.0, SWORD)

      "have a cell" in {
        gladiator.cell should be(Cell(0))
      }
      "have movementPoints" in {
        gladiator.movementPoints should be (10.0)
      }
      "have ap" in {
        gladiator.ap should be (50.0)
      }
      "have hp" in {
        gladiator.hp should be (100.0)
      }
      "have a type " in {
        gladiator.gladiatortype should be (SWORD)
      }
    }
    "after levelUp" should {
      var gladiator2 = Gladiator(Cell(0), 10.0, 50.0, 100.0, SWORD)

      gladiator2.levelUp(50)
      "have more ap" in {
        gladiator2.ap should be (100.0)
      }
    }

  }
}

