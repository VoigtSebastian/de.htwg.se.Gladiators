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
        controller.createRandom(7, 0)

        controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
        controller.addGladiator(1, controller.playingField.size / 2)
        "trying to move a gladiator that is not existing" in {
            controller.categorizeMove(0, 0, 2, 2) should be(MoveType.UNIT_NOT_EXISTING)
        }

        "move an existing gladiator" in {
            controller.gameStatus = GameStatus.P1
            controller.categorizeMove(controller.playingField.size - 2, controller.playingField.size / 2, controller.playingField.size -3, controller.playingField.size / 2) should be(MoveType.LEGAL_MOVE)
        }

        "trying to move a unit that is not owned by the current player " in {
            controller.gameStatus = GameStatus.P1
            controller.categorizeMove(1, controller.playingField.size / 2, 4, 4) should be(MoveType.UNIT_NOT_OWNED_BY_PLAYER)
        }
    }
    "A controller" when {
        val controller = new Controller(PlayingField())
        controller.createRandom(7, 0)

        "add a default gladiator" in {
            controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
            controller.playingField.gladiatorPlayer1(0).line should be (controller.playingField.size - 2)
        }

        "get the basearea of player1" in {
            controller.baseArea(controller.players(0)).size should be (2)
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
