package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.aview.Gui.GuiEvents.TileClicked
import de.htwg.se.gladiators.util.Coordinate
import scala.swing._
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board.BoardLabel
import de.htwg.se.gladiators.model.TileType

case class BoardPanel(length: Int, tiles: Vector[Vector[TileType]]) extends GridPanel(length, length) with Publisher {
    val tileLabels = (0 to (length - 1))
        .map(y => (0 to (length - 1)).map(x => new BoardLabel(x, y, tiles(y)(x))(tileClicked)).toVector)
        .toVector

    tileLabels.foreach(_.foreach(contents += _))

    def tileClicked(tile: Coordinate) = publish(TileClicked(tile))

    def selectTile(coordinate: Coordinate) = tileLabels(coordinate.y)(coordinate.x).select
    def deselectTile(coordinate: Coordinate) = tileLabels(coordinate.y)(coordinate.x).deselect

}
