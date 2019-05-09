package de.htwg.se.gladiators.model.model.aview

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.{Cell, PlayingField}
import org.scalatest.{Matchers, WordSpec}

class TuiSpec extends WordSpec with Matchers {
  val controller = new Controller(PlayingField(new Array[Array[Cell]](3)))
  val tui = new Tui(controller)
  "do nothing on input 'q'" in {
    tui.processInputLine("q")
  }
}
