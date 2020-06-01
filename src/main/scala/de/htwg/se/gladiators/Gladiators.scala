package de.htwg.se.gladiators
import com.google.inject.Guice
import de.htwg.se.gladiators.aview.{Tui, HttpServer}
import de.htwg.se.gladiators.aview.gui.SwingGui
import de.htwg.se.gladiators.controller.controllerComponent.{ControllerInterface, PlayingFieldChanged}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.model.{Cell, CellType}

object Gladiators {

    def main(args: Array[String]): Unit = {
        val RESET_ANSI_ESCAPE = "\033[0m"
        val INPUT_BLUE = "\u001B[34m"
        val WAITING_FOR_INPUT:String = INPUT_BLUE + "▶ " + RESET_ANSI_ESCAPE

        val injector = Guice.createInjector(new GladiatorsModule)
        val controller = injector.getInstance(classOf[ControllerInterface])

        val webserver = new HttpServer(controller)
        val tui = new Tui(controller)
        val gui = new SwingGui(controller)

        controller.publish(new PlayingFieldChanged)
        var input: String  = ""
        if(args.length > 0) input = args(0)
        var output: String = ""
        
        if (!input.isEmpty) tui.processInputLine(input)
        else do {
            print(WAITING_FOR_INPUT)
            input = scala.io.StdIn.readLine()
            tui.processInputLine(input)
        } while (input != "q")
    }
}
