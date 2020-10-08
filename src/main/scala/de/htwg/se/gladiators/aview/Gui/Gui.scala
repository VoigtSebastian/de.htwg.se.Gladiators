package de.htwg.se.gladiators.aview.Gui

import de.htwg.se.gladiators.controller.ControllerInterface
import scala.swing.MainFrame
import scala.swing.event.WindowClosing
import de.htwg.se.gladiators.util.Command.Quit
import de.htwg.se.gladiators.util.Events.Shutdown

class Gui(controller: ControllerInterface) extends MainFrame {
    listenTo(controller)
    title = "Gladiator"

    reactions += {
        case WindowClosing(_) => controller.inputCommand(Quit)
        case Shutdown => ()
    }

    peer.setDefaultCloseOperation(0)
    repaint
    pack
    visible = true
}
