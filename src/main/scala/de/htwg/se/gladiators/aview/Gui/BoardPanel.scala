package de.htwg.se.gladiators.aview.Gui

import scala.swing.GridPanel
import scala.swing.{ Button, Publisher }
import de.htwg.se.gladiators.aview.Gui.GuiEvents.TileClicked
import de.htwg.se.gladiators.util.Coordinate

case class BoardPanel(length: Int) extends GridPanel(length, length) with Publisher {
    (1 to length)
        .foreach(y => (y, (1 to length).foreach(x => contents += Button(f"$x $y") { publish(TileClicked(Coordinate(x, y))) })))
}
