package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.{Cell, GladiatorType, PlayingField}
import de.htwg.se.gladiators.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
    "observed by an Observer" should {
      val controller = new Controller(PlayingField())
      val observer = new Observer {
        var updated: Boolean = false

        def isUpdated: Boolean = updated

        override def update: Unit = {
          updated = true
          updated
        }
      }
      controller.add(observer)
      "notify its Observer after random creation" in {
        controller.createRandom()
        observer.updated should be(true)
      }
      "notify its Observer after adding a gladiator" in {
        controller.addGladiator(0, 0, 10, 10, 100, GladiatorType.SWORD)
        observer.updated should be(true)
        controller.playingField.glad.head.movementPoints should be (10)
      }
      "print a helpmessage" in {
        controller.printHelpMessage()
      }
    }
  }
}
