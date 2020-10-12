package de.htwg.se.gladiators.aview.Gui.GladiatorComponents

import scala.swing._

case class ShowPlayerName(playerDescription: String)(buttonCall: String => Unit) extends BoxPanel(Orientation.Vertical) {
    val textArea = GTextField("")
    contents += Swing.VStrut(2)
    contents += new BorderPanel { add(GLabel(f"Whats the name of $playerDescription"), BorderPanel.Position.Center) }
    contents += Swing.VStrut(4)
    contents += textArea
    contents += Swing.VStrut(4)
    contents += new BorderPanel { add(GButton("Submit")(buttonCall(textArea.text)), BorderPanel.Position.Center) }
    border = Swing.EmptyBorder(8, 8, 8, 8)
}
