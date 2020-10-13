package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Shop

import scala.swing._
import java.awt.Color
import de.htwg.se.gladiators.model.Gladiator

class ShopLabel(number: Int, gladiator: Gladiator)(onClick: Int => Unit) extends Label(f"$number ${gladiator.gladiatorType.simpleString}") {
    font = new Font("Algeria", Font.Bold.id, 16)
    listenTo(mouse.clicks)
    deselect
    reactions += { case _: event.MouseClicked => onClick(number) }
    horizontalTextPosition = Alignment.Center

    def select = (border = Swing.LineBorder(Color.red, 2))
    def deselect = (border = Swing.LineBorder(Color.black, 2))
    def updateText(gladiator: Gladiator) = (text = f"$number ${gladiator.gladiatorType.simpleString}")
}
