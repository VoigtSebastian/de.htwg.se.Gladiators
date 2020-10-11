package de.htwg.se.gladiators.aview.Gui.GladiatorComponents

import scala.swing._
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.GCommon._

object GLabel {
    def apply(text: String) = new Label(text) {
        font = commonFont
        border = commonBorder
    }
}
