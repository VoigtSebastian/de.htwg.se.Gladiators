package de.htwg.se.gladiators

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.util.Events.Init
import com.softwaremill.macwire._
import de.htwg.se.gladiators.aview.Gui.Gui

object Gladiators extends App {
    val controller = wire[Controller]
    val tui = wire[Tui]
    val gui = wire[Gui]

    controller.publish(Init)
    while (tui.processInputLine(scala.io.StdIn.readLine()) && !controller.shouldShutdown.get) {}
}
