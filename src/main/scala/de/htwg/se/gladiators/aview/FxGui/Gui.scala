package de.htwg.se.gladiators.aview.FxGui

import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import de.htwg.se.gladiators.controller.ControllerInterface
import scalafx.stage.StageStyle
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets

case class Gui(controller: ControllerInterface) extends JFXApp {
    stage = new PrimaryStage {
        title = "Gladiators"
        initStyle(StageStyle.Decorated)
        fullScreen_=(true)
        scene = new Scene {
            val pane = new GridPane {
                padding = Insets(10)
                margin = Insets(10)
            }
            (1 to 15).map(x => (1 to 15).map(y =>
                pane.add(
                    new Button {
                        text = s"x$x,y$y"
                        onMousePressed = (_) => { println(s"x$x,y$y") }
                        padding = Insets(5)
                        margin = Insets(5)
                    },
                    x, y)))
            content = pane
        }
    }
}
