package de.htwg.se.gladiators.aview.Gui

import scala.swing.event.Event

import de.htwg.se.gladiators.util.Coordinate

import enumeratum._

sealed trait GuiEvents extends EnumEntry with Event

object GuiEvents extends Enum[GuiEvents] {
    val values = findValues

    case class ShopClicked(item: Int) extends GuiEvents
    case class TileClicked(coordinate: Coordinate) extends GuiEvents
    object EndTurn extends GuiEvents
}
