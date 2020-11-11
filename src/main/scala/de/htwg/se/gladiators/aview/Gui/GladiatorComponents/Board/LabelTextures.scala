package de.htwg.se.gladiators.aview.Gui.GladiatorComponents.Board

import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.model.GladiatorType._
import de.htwg.se.gladiators.model.TileType
import de.htwg.se.gladiators.model.TileType._

import javax.swing.ImageIcon

trait LabelTextures {
    val standardSize = 50
    lazy val baseTexture = resizedTexture("textures/housesand_small.png", standardSize, standardSize)
    lazy val sandTexture = resizedTexture("textures/sand_60.png", standardSize, standardSize)
    lazy val palmTexture = resizedTexture("textures/palmsand_small_color.png", standardSize, standardSize)
    lazy val mineTexture = resizedTexture("textures/sandgold_small.png", standardSize, standardSize)
    lazy val archerPlayerOneTexture = resizedTexture(f"textures/sandbow_small_p1.png", standardSize, standardSize)
    lazy val archerPlayerTwoTexture = resizedTexture(f"textures/sandbow_small_p2.png", standardSize, standardSize)
    lazy val knightPlayerOneTexture = resizedTexture(f"textures/sandsword_small_p1.png", standardSize, standardSize)
    lazy val knightPlayerTwoTexture = resizedTexture(f"textures/sandsword_small_p2.png", standardSize, standardSize)
    lazy val tankPlayerOneTexture = resizedTexture(f"textures/sandshield_small_p1.png", standardSize, standardSize)
    lazy val tankPlayerTwoTexture = resizedTexture(f"textures/sandshield_small_p1.png", standardSize, standardSize)

    def tileTypeToTexture(tileType: TileType) = {
        tileType match {
            case Base => baseTexture
            case Sand => sandTexture
            case Palm => palmTexture
            case Mine(_) => mineTexture
        }
    }

    def gladiatorTexture(gladiatorType: GladiatorType, playerOne: Boolean) = {
        (gladiatorType, playerOne) match {
            case (Archer, true) => archerPlayerOneTexture
            case (Archer, false) => archerPlayerTwoTexture
            case (Knight, true) => knightPlayerOneTexture
            case (Knight, false) => knightPlayerTwoTexture
            case (Tank, true) => tankPlayerOneTexture
            case (Tank, false) => tankPlayerTwoTexture
        }
    }

    def resizedTexture(path: String, width: Int, height: Int): ImageIcon = {
        val image = new ImageIcon(path)
            .getImage
            .getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH)
        new ImageIcon(image);
    }
}
