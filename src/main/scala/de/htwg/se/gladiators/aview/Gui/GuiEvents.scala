package de.htwg.se.gladiators.aview.Gui

import scala.swing.event.Event
import enumeratum._

sealed trait Events extends EnumEntry with Event

object GuiEvents extends Enum[Events] {
    val values = findValues

    case class ShopClicked(item: Int) extends Events
}
