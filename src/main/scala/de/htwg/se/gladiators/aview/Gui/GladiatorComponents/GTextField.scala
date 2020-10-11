package de.htwg.se.gladiators.aview.Gui.GladiatorComponents

import scala.swing._
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.GCommon._

object GTextField {
    def apply(text: String) = new TextField(text) {
        font = commonFont
        border = commonBorder
    }
}
