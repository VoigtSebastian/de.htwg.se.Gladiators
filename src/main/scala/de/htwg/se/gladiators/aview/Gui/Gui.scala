package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.aview.Gui.GuiEvents._
import de.htwg.se.gladiators.util.Command.Quit
import de.htwg.se.gladiators.util.{ Configuration, Coordinate }
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Command

import scala.swing.event.WindowClosing
import scala.swing._
import de.htwg.se.gladiators.aview.Gui.GladiatorComponents._

class Gui(controller: ControllerInterface, configuration: Configuration) extends MainFrame with Reactor with Publisher {
    listenTo(controller)
    title = "Gladiator"
    peer.setDefaultCloseOperation(0)
    peer.setLocationRelativeTo(null)

    implicit class BetterComponents[T <: Component](component: T) {
        def listenAndReturn = {
            listenTo(component)
            component
        }
    }

    var selectedShopItem: Option[Int] = None
    var selectedTile: Option[Coordinate] = None

    val shopPanel = ShopPanel(controller.stock).listenAndReturn
    val boardPanel = BoardPanel(configuration.boardSize, controller.boardTiles).listenAndReturn

    reactions += {
        case WindowClosing(_) => controller.inputCommand(Quit)
        case Events.Init => showNamingPlayerOne
        case Events.PlayerOneNamed(_) => showNamingPlayerTwo
        case Events.PlayerTwoNamed(_) => showGame

        case Events.Moved(player, from, to, gladiator) => {
            boardPanel.addGladiator(to, gladiator, (player.enemyBaseLine > 0))
            boardPanel.removeGladiator(from)
            resetSelected
        }
        case Events.SuccessfullyBoughtGladiator(player, gladiator) => {
            if (selectedShopItem != None) shopPanel.deselectItem(selectedShopItem.get)
            boardPanel.addGladiator(gladiator.position, gladiator, (player.enemyBaseLine > 0))
        }
    }

    repaint
    pack
    visible = true

    def showNamingPlayerOne = contents = ShowPlayerName("Player One") { (text: String) => controller.inputCommand(Command.NamePlayerOne(text)) }
    def showNamingPlayerTwo = contents = ShowPlayerName("Player Two") { (text: String) => controller.inputCommand(Command.NamePlayerTwo(text)) }

    def showGame = {
        contents = new BoxPanel(Orientation.Horizontal) {
            contents += shopPanel
            contents += boardPanel
        }

        reactions += {
            case ShopClicked(number) if Some(number) != selectedShopItem => {
                selectedShopItem = Some(number)
                shopPanel.selectItem(number)
                resetSelectedTile
            }
            case ShopClicked(_) => {
                shopPanel.deselectItem(selectedShopItem.get)
                selectedShopItem = None
            }

            case TileClicked(newTile) =>
                (selectedTile, selectedShopItem) match {
                    case (None, None) => if (controller.tileOccupiedByCurrentPlayer(newTile)) selectTile(newTile)
                    case (Some(_), None) if selectedTile.get != newTile => {
                        move(newTile)
                        resetSelected
                        deselectTile(newTile)
                    }
                    case (Some(_), None) => resetSelected
                    case (None, Some(_)) => if (controller.newUnitPlacementTiles.get.contains(newTile)) {
                        selectTile(newTile)
                        controller.buyUnit(selectedShopItem.get, newTile)
                        resetSelected
                    }
                    case (Some(_), Some(_)) => ()
                }
        }
    }

    def selectTile(coordinate: Coordinate) = {
        selectedTile = Some(coordinate)
        boardPanel.selectTile(coordinate)
    }

    def resetSelected = {
        resetSelectedTile
        selectedShopItem = None
    }

    def resetSelectedTile = {
        if (selectedTile != None)
            deselectTile(selectedTile.get)
        selectedTile = None
    }

    def deselectTile(coordinate: Coordinate) = boardPanel.deselectTile(coordinate)

    def move(to: Coordinate) = controller.move(selectedTile.get, to)
}
