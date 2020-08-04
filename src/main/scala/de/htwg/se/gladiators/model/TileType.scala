package de.htwg.se.gladiators.model

import enumeratum._
import scalafx.scene.control.Button
import javafx.scene.control
import scalafx.scene.image.ImageView
import java.io.File
import scalafx.geometry.Insets

sealed trait TileType extends EnumEntry {
    // val image = new ImageView(new File("textures/sand_60.png").toURI.toString)
    def toButton(x: Int, y: Int, call: (Int, Int) => Unit): Button = new Button {
        onMousePressed = (event) => {
            println(event.getSource.asInstanceOf[control.Button])
            call(x, y)
        }
        graphic = new ImageView(new File("textures/sand_60.png").toURI.toString)
        prefHeight = 61
        prefWidth = 61
        padding = Insets(0)
        margin = Insets(0)
    }
}

object TileType extends Enum[TileType] {
    val values = findValues

    case object Sand extends TileType
    case object Palm extends TileType
    case object Base extends TileType
    case class Mine(gold: Int) extends TileType
}
