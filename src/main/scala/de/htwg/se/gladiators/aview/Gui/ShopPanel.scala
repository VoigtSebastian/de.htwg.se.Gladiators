package de.htwg.se.gladiators.aview.Gui

import scala.swing.{ Orientation, BoxPanel, Button }
import scala.swing.Publisher
import de.htwg.se.gladiators.aview.Gui.GuiEvents.ShopClicked

case class ShopPanel(stock: Int) extends BoxPanel(Orientation.Vertical) with Publisher {
    (1 to stock)
        .foreach(n => contents += new Button(f"Gladiator $n") { publish(ShopClicked(n)) })
}
