package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.aview.Gui.GuiEvents._
import de.htwg.se.gladiators.util.Command.Quit
import scala.swing.event.WindowClosing
import scala.swing._

class Gui(controller: ControllerInterface) extends MainFrame {
    listenTo(controller)
    title = "Gladiator"
    var selectedShopItem: Option[Int] = None

    contents = new BoxPanel(Orientation.Horizontal) {
        contents += listenToContents(ShopPanel(10))
        contents += Button("Press me, please") { println("Thank you") }
    }

    reactions += {
        case ShopClicked(number) => selectedShopItem = Some(number)
        case WindowClosing(_) => controller.inputCommand(Quit)
    }

    peer.setDefaultCloseOperation(0)
    repaint
    pack
    visible = true

    def listenToContents(component: Component) = {
        listenTo(component)
        component
    }
}
