package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board

import de.htwg.se.gladiators.util.Coordinate
import scala.swing._
import java.awt.Color

object BoardLabel {
    def apply(x: Int)(y: Int)(onClick: Coordinate => Unit) = new Label(f"$x $y") {
        font = new Font("Algeria", Font.Plain.id, 14)
        listenTo(mouse.clicks)
        deselect
        reactions += { case _: event.MouseClicked => onClick(Coordinate(x, y)) }
        def select = (border = Swing.LineBorder(Color.red, 4))
        def deselect = (border = Swing.LineBorder(Color.black, 2))
    }
}
