package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.aview.Gui.GuiEvents._
import de.htwg.se.gladiators.util.Command.Quit
import de.htwg.se.gladiators.util.Configuration
import scala.swing.event.WindowClosing
import scala.swing._
import de.htwg.se.gladiators.util.Coordinate

class Gui(controller: ControllerInterface, configuration: Configuration) extends MainFrame with Reactor {
    listenTo(controller)
    title = "Gladiator"
    var selectedShopItem: Option[Int] = None
    var selectedTile: Option[Coordinate] = None

    val shopPanel = ShopPanel(configuration.itemsInShop).listenAndReturn
    val boardPanel = BoardPanel(configuration.boardSize).listenAndReturn

    implicit class BetterComponents[T <: Component](component: T) {
        def listenAndReturn = {
            listenTo(component)
            component
        }
    }

    contents = new BoxPanel(Orientation.Horizontal) {
        contents += shopPanel
        contents += boardPanel
    }

    reactions += {
        case WindowClosing(_) => controller.inputCommand(Quit)

        case ShopClicked(number) if Some(number) != selectedShopItem => {
            selectedShopItem = Some(number)
            selectedTile = None
            println(f"Shop clicked $number")
        }
        case ShopClicked(_) => selectedShopItem = None

        case TileClicked(newTile) =>
            (selectedTile, selectedShopItem) match {
                case (None, None) => selectTile(newTile)
                case (Some(_), None) if selectedTile.get != newTile => {
                    move(newTile)
                    resetSelected
                    deselectTile(newTile)
                }
                case (Some(_), None) => resetSelected
                case (None, Some(_)) => {
                    selectTile(newTile)
                    println(f"Buy unit ${selectedShopItem.get} to ${newTile}")
                    resetSelected
                }
                case (Some(_), Some(_)) => ()
            }
    }

    peer.setDefaultCloseOperation(0)
    repaint
    pack
    visible = true

    def selectTile(coordinate: Coordinate) = {
        selectedTile = Some(coordinate)
        boardPanel.selectTile(coordinate)
    }

    def resetSelected = {
        deselectTile(selectedTile.get)
        selectedTile = None
        selectedShopItem = None
    }

    def deselectTile(coordinate: Coordinate) = boardPanel.deselectTile(coordinate)

    def move(to: Coordinate) = println(f"Move from ${selectedTile.get} to $to")
}
