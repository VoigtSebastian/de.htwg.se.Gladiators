package de.htwg.se.gladiators.aview.Gui

import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Publisher

import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Shop.ShopLabel
import de.htwg.se.gladiators.aview.Gui.GuiEvents.ShopClicked
import de.htwg.se.gladiators.model.Gladiator

case class ShopPanel(stock: Vector[Gladiator]) extends BoxPanel(Orientation.Vertical) with Publisher {
    val shopButtons = (1 to stock.length)
        .map(n => new ShopLabel(n, stock(n - 1))(gladiatorClicked))
    shopButtons.foreach(label => contents += new BorderPanel { add(label, BorderPanel.Position.Center) })

    def gladiatorClicked(number: Int) = publish(ShopClicked(number))
    def selectItem(number: Int) = shopButtons(number - 1).select
    def deselectItem(number: Int) = shopButtons(number - 1).deselect
    def updateItems(newStock: Vector[Gladiator]) = newStock
        .zip(shopButtons)
        .foreach({ case (newGladiator, button) => button.updateText(newGladiator) })
}
