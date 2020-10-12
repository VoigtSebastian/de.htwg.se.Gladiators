package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board

import de.htwg.se.gladiators.util.Coordinate
import scala.swing._
import javax.swing.ImageIcon
import java.awt.Color
import de.htwg.se.gladiators.model.TileType
import de.htwg.se.gladiators.model.TileType._

class BoardLabel(x: Int, y: Int, tileType: TileType)(onClick: Coordinate => Unit) extends Label(f"$x $y") {
    font = new Font("Algeria", Font.Plain.id, 14)
    listenTo(mouse.clicks)
    deselect
    reactions += { case _: event.MouseClicked => onClick(Coordinate(x, y)) }
    horizontalTextPosition = Alignment.Center

    changeTileType(tileType)

    def select = (border = Swing.LineBorder(Color.red, 2))
    def deselect = (border = Swing.LineBorder(Color.black, 2))

    def changeTileType(newTileType: TileType) = {
        newTileType match {
            case Base => icon = resizedTexture("textures/housesand_small.png", 40, 40)
            case Sand => icon = resizedTexture("textures/sand_60.png", 40, 40)
            case Palm => icon = resizedTexture("textures/palmsand_small_color.png", 40, 40)
            case Mine(_) => icon = resizedTexture("textures/sand_60.png", 40, 40)
        }
    }
    def resizedTexture(path: String, width: Int, height: Int): ImageIcon = {
        val image = new ImageIcon(path)
            .getImage
            .getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)
        new ImageIcon(image);
    }
}
