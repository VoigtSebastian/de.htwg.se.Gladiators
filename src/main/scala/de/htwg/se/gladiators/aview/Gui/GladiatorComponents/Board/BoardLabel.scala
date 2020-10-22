package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board

import de.htwg.se.gladiators.util.Coordinate
import scala.swing._
import java.awt.Color
import de.htwg.se.gladiators.model.TileType
import de.htwg.se.gladiators.model.GladiatorType

class BoardLabel(x: Int, y: Int, tileType: TileType)(onClick: Coordinate => Unit) extends Label("") with LabelTextures {
    font = new Font("Algeria", Font.Italic.id, 14)
    listenTo(mouse.clicks)
    deselect
    reactions += { case _: event.MouseClicked => onClick(Coordinate(x, y)) }
    horizontalTextPosition = Alignment.Center

    changeTileType(tileType)

    def select = (border = Swing.LineBorder(Color.red, 2))
    def deselect = (border = Swing.LineBorder(Color.black, 2))

    def changeTileType(newTileType: TileType) = icon = tileTypeToTexture(newTileType)
    def addGladiator(gladiatorType: GladiatorType, playerOne: Boolean) = icon = gladiatorTexture(gladiatorType, playerOne)
}
