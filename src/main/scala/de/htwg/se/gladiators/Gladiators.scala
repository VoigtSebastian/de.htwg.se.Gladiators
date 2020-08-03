package de.htwg.se.gladiators

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.util.Events.Init
import de.htwg.se.gladiators.aview.FxGui.Gui
import javafx.embed.swing.JFXPanel
import javafx.application.Platform

object Main extends App {
    val controller = Controller(15)
    val tui = Tui(controller)

    new JFXPanel()
    Platform.runLater(new Runnable { def run() = Gui(controller).show() })

    controller.publish(Init)
    while (tui.processInputLine(scala.io.StdIn.readLine())) {}
}
