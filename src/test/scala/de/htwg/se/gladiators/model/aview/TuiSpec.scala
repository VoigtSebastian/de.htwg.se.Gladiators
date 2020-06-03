package de.htwg.se.gladiators.model.aview

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.controllerComponent.GameStatus
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.model.{Cell, Gladiator, GladiatorType}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {

    //val controller = new Controller(PlayingField())
    val controller = new Controller()

    val tui = new Tui(controller)
   // controller.createRandom(7,0)
    "do nothing on input 'q'" in {
        tui.processInputLine("q")
    }
    "create a random playingField" in {
        tui.processInputLine("n")
        controller.playingField.getSize should be(PlayingField().size)
    }

    "add a gladiator to the playingField" in {
        controller.createRandom(15,0)
        controller.gameStatus = GameStatus.P1
        controller.selectedGlad = controller.shop.stock.head._1
        val input = "b,1," + (controller.playingField.size - 2).toString + "," + (controller.playingField.size / 2).toString
        tui.processInputLine(input)
        controller.playingField.gladiatorPlayer1.head.line should be(controller.playingField.size - 2)
    }
    "show a help message" in {
        tui.processInputLine("h")
    }
}