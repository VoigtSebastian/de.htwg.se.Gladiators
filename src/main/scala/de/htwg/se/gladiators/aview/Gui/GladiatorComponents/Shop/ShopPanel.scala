package de.htwg.se.gladiators.aview.Gui

import scala.swing.{ Orientation, BoxPanel, BorderPanel, Publisher }
import de.htwg.se.gladiators.aview.Gui.GuiEvents.ShopClicked
import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Shop.ShopLabel

case class ShopPanel(stock: Vector[Gladiator]) extends BoxPanel(Orientation.Vertical) with Publisher {
    val shopButtons = (1 to stock.length)
        .map(n => new ShopLabel(n, stock(n - 1))(gladiatorClicked))
    shopButtons.foreach(label => contents += new BorderPanel { add(label, BorderPanel.Position.Center) })

    def gladiatorClicked(number: Int) = publish(ShopClicked(number))
    def selectItem(number: Int) = shopButtons(number - 1).select
    def deselectItem(number: Int) = shopButtons(number - 1).deselect
    def updateItem(number: Int) = ???
}
