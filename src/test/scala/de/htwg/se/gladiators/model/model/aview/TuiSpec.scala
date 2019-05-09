package de.htwg.se.gladiators.model.model.aview

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.{Cell, Gladiator, GladiatorType, PlayingField}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
  val controller = new Controller(PlayingField())
  val tui = new Tui(controller)
  "do nothing on input 'q'" in {
    tui.processInputLine("q")
  }
  "create a random playingfield" in {
    tui.processInputLine("n")
    controller.playingField.size() should be (7)
  }

  "add a gladiator to the playingfield" in {
    tui.processInputLine("g,0,0")
    controller.playingField.glad.head should be (Gladiator(0,0,10,10,10,GladiatorType.SWORD))
  }

}
