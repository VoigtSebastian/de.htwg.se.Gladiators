package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.{Cell, GladiatorType, PlayingField}
import de.htwg.se.gladiators.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
    "A controller" when {
        val controller = new Controller(PlayingField())
        val xLegal = 5
        val yLegal = 5

        val xIllegal = 10
        val yIllegal = 12
        "when checking coordinates " + xLegal + " " + yLegal in {
            controller.isCoordinateLegal(xLegal, yLegal) should be(true)
            controller.isCoordinateLegal(xIllegal, yIllegal) should be(false)
        }
    }

    //TODO: ControllerSpecUpdate
    //"A Controller" when {
    //    "observed by an Observer" should {
    //        val controller = new Controller(PlayingField())
    //
    //        val observer = new Observer {
    //            var updated: Boolean = false
    //
    //            def isUpdated: Boolean = updated
    //
    //            override def update: Unit = {
    //                updated = true
    //                print(controller.printPlayingField())
    //                //updated
    //            }
    //        }
    //        controller.add(observer)
    //        "notify its Observer after random creation" in {
    //            controller.createRandom() observer.updated should be(true)
    //        }
    //        "notify its Observer after adding a gladiator" in {
    //            controller.addGladiator(0, 0, GladiatorType.SWORD)
    //            controller.addGladiator(1, 1, GladiatorType.BOW)
    //            controller.addGladiator(2, 2, GladiatorType.TANK)
    //            observer.updated should be(true)
    //            controller.playingField.gladiatorPlayer1.head.movementPoints should be(3)
    //        }
    //    }
    //}

}
