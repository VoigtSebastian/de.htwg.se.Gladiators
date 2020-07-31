package de.htwg.se.gladiators

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.BaseImplementation.Controller

object Main extends App {
    val tui = Tui(Controller())

    while (tui.processInputLine(scala.io.StdIn.readLine())) {}
}
