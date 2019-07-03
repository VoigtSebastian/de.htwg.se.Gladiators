package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.controller.controllerComponent.{CommandStatus, GameStatus, MoveType}
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.model.{Cell, CellType, GladiatorType}
import de.htwg.se.gladiators.util.Observer
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers {
    "A controller" when {
        val controller = new Controller()
        controller.createRandom(15)
        val xLegal = 5
        val yLegal = 5

        val xIllegal = 20
        val yIllegal = 12
        "checking coordinates " + xLegal + " " + yLegal in {
            controller.isCoordinateLegal(xLegal, yLegal) should be(true)
            controller.isCoordinateLegal(xIllegal, yIllegal) should be(false)
        }
    }


    "A controller " when {
        val controller = new Controller()
        controller.createRandom(15, 0)

        controller.selectedGlad = controller.shop.stock.head._1
        controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
        controller.endTurn()
        controller.selectedGlad = controller.shop.stock.head._1
        controller.addGladiator(1, controller.playingField.size / 2)
        "trying to move a gladiator that is not existing" in {
            controller.categorizeMove(0, 0, 2, 2) should be(MoveType.UNIT_NOT_EXISTING)
        }

        "move an existing gladiator" in {
            controller.gameStatus = GameStatus.P1
            controller.playingField.gladiatorPlayer1.head.moved = false
            controller.categorizeMove(controller.playingField.size - 2, controller.playingField.size / 2, controller.playingField.size -3, controller.playingField.size / 2) should be(MoveType.LEGAL_MOVE)
        }

        "trying to move a unit that is not owned by the current player " in {
            controller.gameStatus = GameStatus.P1
            controller.categorizeMove(1, controller.playingField.size / 2, 4, 4) should be(MoveType.UNIT_NOT_OWNED_BY_PLAYER)
        }
    }
    "A controller" when {
        val controller = new Controller()
        controller.createRandom(15, 0)
        controller.selectedGlad = controller.shop.stock.head._1

        "add a default gladiator" in {
            controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
            controller.playingField.gladiatorPlayer1.head.line should be (controller.playingField.size - 2)
        }

        "get the basearea of player1" in {
            controller.baseArea(controller.players(0)).size should be (2)
        }

        "get the base cell of player1 and player 2" in {
            controller.getBase(controller.players(0)) should be (controller.playingField.size - 1, controller.playingField.size / 2)
            controller.getBase(controller.players(1)) should be (0, controller.playingField.size / 2)
        }

        "can reset the game" in {
            controller.resetGame()
            controller.playingField.gladiatorPlayer1.size should be (0)
            controller.playingField.gladiatorPlayer2.size should be (0)
        }

        "can print the shop" in {
            controller.getShop.contains("Units available in the shop:") should be (true)
        }

        "can get the info of a Gladiator" in {
            controller.gladiatorInfo(controller.playingField.size - 2, controller.playingField.size / 2).contains("is owned by") should be (true)
        }

        "can toggle the unitstats" in {
            controller.toggleUnitStats()
        }

        "can get a cell" in {
            controller.cell(controller.playingField.size - 1, controller.playingField.size / 2).cellType should be (CellType.BASE)
        }

        "can inform the GUI/TUI that a cell was selected" in {
            controller.cellSelected(5,5)
        }

        "can also change the commandStatus" in {
            controller.changeCommand(CommandStatus.MV)
            controller.commandStatus should be (CommandStatus.MV)
        }

        "can check if gladiator is in a list" in {
            controller.isGladiatorInList(controller.playingField.gladiatorPlayer1, controller.playingField.size - 2, controller.playingField.size / 2 )
        }

    }
    "A controller " when {
        val pf = PlayingField(7)
        val controller = new Controller()
        controller.createRandom(15, 0)

        controller.selectedGlad = controller.shop.stock.head._1
        controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
        controller.endTurn()
        controller.selectedGlad = controller.shop.stock.head._1
        controller.addGladiator(1, controller.playingField.size / 2)

        "can tell a gladiator to attack another gladiator" in {
            controller.attack(controller.playingField.size - 2, controller.playingField.size / 2, 1, controller.playingField.size / 2)._1 should be (false)
        }
    }
}
