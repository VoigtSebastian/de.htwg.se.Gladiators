package de.htwg.se.gladiators.aview.FxGui

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{ Stops, LinearGradient }
import scalafx.scene.text.Text
import de.htwg.se.gladiators.controller.ControllerInterface
import scalafx.stage.Stage
import scalafx.stage.StageStyle

case class Gui(controller: ControllerInterface) extends Stage {
    title = "ScalaFX Hello World"
    initStyle(StageStyle.Decorated)
    // override def style: StageStyle = StageStyle.Decorated
    scene = new Scene {
        fill = Black
        content = new HBox {
            padding = Insets(20)
            children = Seq(
                new Text {
                    text = "Hello "
                    style = "-fx-font-size: 48pt"
                    fill = new LinearGradient(
                        endX = 0,
                        stops = Stops(PaleGreen, SeaGreen))
                },
                new Text {
                    text = "World!!!"
                    style = "-fx-font-size: 48pt"
                    fill = new LinearGradient(
                        endX = 0,
                        stops = Stops(Cyan, DodgerBlue))
                    effect = new DropShadow {
                        color = DodgerBlue
                        radius = 25
                        spread = 0.25
                    }
                })
        }
    }
}
