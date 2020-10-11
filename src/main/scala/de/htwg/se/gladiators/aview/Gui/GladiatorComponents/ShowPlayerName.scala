package de.htwg.se.gladiators.aview.Gui.GladiatorComponents

import scala.swing._

case class ShowPlayerName(playerDescription: String)(buttonCall: String => Unit) extends BoxPanel(Orientation.Vertical) {
    val textArea = GTextField("")
    contents += Swing.VStrut(2)
    contents += GLabel(f"Whats the name of $playerDescription")
    contents += Swing.VStrut(2)
    contents += textArea
    contents += Swing.VStrut(2)
    contents += GButton("Submit")(buttonCall(textArea.text))
    border = Swing.EmptyBorder(2, 2, 2, 2)
}
