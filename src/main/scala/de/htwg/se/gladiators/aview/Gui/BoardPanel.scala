package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.aview.Gui.GuiEvents.TileClicked
import de.htwg.se.gladiators.util.Coordinate
import java.awt.Color
import scala.swing._

case class BoardPanel(length: Int) extends GridPanel(length, length) with Publisher {
    val tiles = (0 to (length - 1))
        .map(y => (0 to (length - 1)).map(x => new Label(f"$x $y") {
            listenTo(mouse.clicks)
            deselect
            reactions += { case _: event.MouseClicked => tileClicked(Coordinate(x, y)) }

            def select = (border = Swing.LineBorder(Color.red, 4))
            def deselect = (border = Swing.LineBorder(Color.black, 2))
        }).toVector)
        .toVector

    tiles.foreach(_.foreach(contents += _))

    def tileClicked(tile: Coordinate) = publish(TileClicked(Coordinate(tile.x, tile.y)))

    def selectTile(coordinate: Coordinate) = tiles(coordinate.y)(coordinate.x).select
    def deselectTile(coordinate: Coordinate) = tiles(coordinate.y)(coordinate.x).deselect

}
