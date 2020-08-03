package de.htwg.se.gladiators.aview.FxGui

import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import de.htwg.se.gladiators.controller.ControllerInterface
import scalafx.stage.StageStyle
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.control.Button
import scalafx.scene.layout.{ HBox, VBox }

case class Gui(controller: ControllerInterface) extends JFXApp {
    stage = new PrimaryStage {
        title = "Gladiators"
        initStyle(StageStyle.Decorated)
        fullScreen_=(true)
        scene = new Scene {
            fill = Black
            content = new VBox {
                fillWidth = true
                children = (1 to 15)
                    .map(y => new HBox {
                        fillWidth = true
                        children = (1 to 15)
                            .map(x => new Button {
                                text = s"$y $x"
                                onMousePressed = (_) => {
                                    println(s"$y $x")
                                }
                            }).seq
                    }).seq
            }
        }
    }
}
