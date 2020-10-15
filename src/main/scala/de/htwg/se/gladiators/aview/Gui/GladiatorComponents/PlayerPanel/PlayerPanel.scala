package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.PlayerPanel

import scala.swing._
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.GButton
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.GLabel
import de.htwg.se.gladiators.aview.Gui.GuiEvents.EndTurn

case class PlayerPanel(playerName: String = "", credits: Int = 0) extends BoxPanel(Orientation.Horizontal) with Publisher {
    val nameLabel = GLabel(playerName)
    val creditsLabel = GLabel("$" + credits)

    contents += new BorderPanel { add(nameLabel, BorderPanel.Position.Center) }
    contents += new BorderPanel { add(creditsLabel, BorderPanel.Position.Center) }
    contents += new BorderPanel { add(GButton("End Turn") { endTurn }, BorderPanel.Position.Center) }

    def turn(newName: String, newCredits: Int): Unit = {
        nameLabel.text = newName
        creditsLabel.text = "$" + newCredits
    }
    def endTurn = publish(EndTurn)
}
