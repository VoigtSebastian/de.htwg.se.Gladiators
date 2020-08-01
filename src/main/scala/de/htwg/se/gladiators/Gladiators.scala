package de.htwg.se.gladiators

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.util.Events.Init

object Main extends App {
    val controller = Controller(15)
    val tui = Tui(controller)

    controller.publish(Init)
    while (tui.processInputLine(scala.io.StdIn.readLine())) {}
}
