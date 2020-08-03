package de.htwg.se.gladiators

import de.htwg.se.gladiators.aview.Tui
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.util.Events.Init
import de.htwg.se.gladiators.aview.FxGui.Gui
import scala.concurrent.{ Future, blocking }
import scala.concurrent.ExecutionContext

object Main extends App {
    implicit val ec = ExecutionContext.global

    val controller = Controller(15)

    val tui = Tui(controller)
    Future { blocking { Gui(controller).main(args) } }

    controller.publish(Init)
    while (tui.processInputLine(scala.io.StdIn.readLine())) {}
}
