package de.htwg.se.gladiators.aview.Gui

import scala.swing.event.Event
import enumeratum._
import de.htwg.se.gladiators.util.Coordinate

sealed trait GuiEvents extends EnumEntry with Event

object GuiEvents extends Enum[GuiEvents] {
    val values = findValues

    case class ShopClicked(item: Int) extends GuiEvents
    case class TileClicked(coordinate: Coordinate) extends GuiEvents
}
