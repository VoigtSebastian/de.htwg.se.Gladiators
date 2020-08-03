package de.htwg.se.gladiators.aview.FxGui

import scalafx.scene.Scene
import de.htwg.se.gladiators.controller.ControllerInterface
import scalafx.stage.StageStyle
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.layout.GridPane
import de.htwg.se.gladiators.model.TileType.Sand

case class Gui(controller: ControllerInterface) extends JFXApp {
    stage = new PrimaryStage {
        title = "Gladiators"
        initStyle(StageStyle.Decorated)
        fullScreen_=(true)
        scene = new Scene {
            val pane = new GridPane {
                // padding = Insets(10)
                // margin = Insets(10)
            }
            (1 to 15).map(x => (1 to 15).map(y =>
                pane.add(Sand.toButton(x, y, callOnButtonPressed), x, y)))
            content = pane
        }
    }

    def callOnButtonPressed(x: Int, y: Int) = println(s"x$x,y$y")
}
