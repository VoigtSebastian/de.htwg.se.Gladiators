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

    implicit class BetterComponents(component: Component) {
        def listenAndReturn = {
            listenTo(component)
            component
        }
    }

    contents = new BoxPanel(Orientation.Horizontal) {
        contents += ShopPanel(configuration.itemsInShop).listenAndReturn
        contents += BoardPanel(configuration.boardSize).listenAndReturn
    }

    reactions += {
        case WindowClosing(_) => controller.inputCommand(Quit)

        case ShopClicked(number) => {
            selectedShopItem = Some(number)
            println(f"Shop clicked $number")
        }

        case TileClicked(newTile) => (selectedTile, selectedShopItem) match {
            case (None, None) => { selectedTile = Some(newTile) }
            case (Some(_), None) if selectedTile.get != newTile => {
                move(newTile)
                resetSelected
            }
            case (Some(_), None) => selectedTile = None
            case (None, Some(_)) => {
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

    def resetSelected = {
        selectedTile = None
        selectedShopItem = None
    }

    def move(to: Coordinate) = println(f"Move from ${selectedTile.get} to $to")
}
