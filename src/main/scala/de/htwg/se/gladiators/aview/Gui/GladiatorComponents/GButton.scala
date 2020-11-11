package de.htwg.se.gladiators.aview.Gui.GladiatorComponents

import scala.swing._

import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.GCommon._

object GButton {
    def apply(text: String)(op: => Unit) = {
        val button = Button(text)(op)
        button.font = commonFont
        button.border = commonBorder
        button
    }
}
