package de.htwg.se.gladiators.model.model.aview

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.{Controller, GameStatus}
import de.htwg.se.gladiators.model.{Cell, Gladiator, GladiatorType, PlayingField}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
    val controller = new Controller(PlayingField())
    val tui = new Tui(controller)
    controller.createRandom(7,0)
    "do nothing on input 'q'" in {
        tui.processInputLine("q")
    }
    "create a random playingfield" in {
        tui.processInputLine("n")
        controller.playingField.getSize should be(9)
    }

    "add a gladiator to the playingfield" in {
        controller.gameStatus = GameStatus.P1
        tui.processInputLine("g,7,4")
        controller.playingField.gladiatorPlayer1.head.line should be(7)
    }
    "show a help message" in {
        tui.processInputLine("h")
    }
}
