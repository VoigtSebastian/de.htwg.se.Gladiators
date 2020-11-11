package de.htwg.se.gladiators.aview.Gui

import scala.swing._

import de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board.BoardLabel
import de.htwg.se.gladiators.aview.Gui.GuiEvents.TileClicked
import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.model.TileType
import de.htwg.se.gladiators.util.Coordinate

case class BoardPanel(length: Int, tiles: Vector[Vector[TileType]]) extends GridPanel(length, length) with Publisher {
    val tileLabels = (0 to (length - 1))
        .map(y => (0 to (length - 1)).map(x => new BoardLabel(x, y, tiles(y)(x))(tileClicked)).toVector)
        .toVector

    tileLabels.foreach(_.foreach(contents += _))

    def tileClicked(tile: Coordinate) = publish(TileClicked(tile))

    def addGladiator(coordinate: Coordinate, gladiator: Gladiator, playerOne: Boolean): Unit = {
        tileLabels(coordinate.y)(coordinate.x)
            .addGladiator(gladiator.gladiatorType, playerOne)
    }
    def removeGladiator(coordinate: Coordinate, tileType: TileType = TileType.Sand): Unit = {
        tileLabels(coordinate.y)(coordinate.x)
            .changeTileType(tileType)
    }
    def selectTile(coordinate: Coordinate): Unit = tileLabels(coordinate.y)(coordinate.x).select
    def deselectTile(coordinate: Coordinate): Unit = tileLabels(coordinate.y)(coordinate.x).deselect

}
