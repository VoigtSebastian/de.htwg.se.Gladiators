package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.{Controller, GameStatus, MoveType}
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
        "checking coordinates " + xLegal + " " + yLegal in {
            controller.isCoordinateLegal(xLegal, yLegal) should be(true)
            controller.isCoordinateLegal(xIllegal, yIllegal) should be(false)
        }
    }


    "A controller " when {
        val pf = PlayingField()
        val controller = new Controller(pf)

        controller.addGladiator(0, 0, GladiatorType.SWORD)
        controller.addGladiator(1, 1, GladiatorType.SWORD)
        controller.addGladiator(2, 2, GladiatorType.SWORD)
        controller.addGladiator(3, 3, GladiatorType.SWORD)

        "trying to move to/attack a blocked position" in {
            controller.gameStatus = GameStatus.P1
            controller.categorizeMove(0, 0, 2,2 ) should be(MoveType.BLOCKED)
        }

        "trying to move a unit that is not owned by the current player " in {
            controller.gameStatus = GameStatus.P1
            controller.categorizeMove(1,1,4,4)  should be (MoveType.UNIT_NOT_OWNED_BY_PLAYER)
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
