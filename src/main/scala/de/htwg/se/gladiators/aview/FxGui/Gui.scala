package de.htwg.se.gladiators.aview.FxGui

import scalafx.scene.Scene
import de.htwg.se.gladiators.controller.ControllerInterface
import scalafx.stage.StageStyle
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.layout.GridPane
import de.htwg.se.gladiators.model.TileType.Sand
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Command.Move
import javafx.scene.layout.{ GridPane => JGridPane }
import javafx.scene.Node

case class Gui(controller: ControllerInterface) extends JFXApp {
    val board = new GridPane {}
    var selectedTile: Option[Coordinate] = None

    stage = new PrimaryStage {
        title = "Gladiators"
        initStyle(StageStyle.Decorated)
        fullScreen_=(true)
        scene = new Scene {
            (1 to 15).map(x => (1 to 15).map(y =>
                board.add(Sand.toButton(x, y, callOnTilePressed), x, y)))
            content = board
        }
    }

    def callOnTilePressed(x: Int, y: Int) = {
        (selectedTile, Coordinate(x, y)) match {
            case (None, newCoordinate) => {
                board
                    .getChildren
                    .filtered(button => buttonByCoordinate(button, newCoordinate))
                    .forEach(button => button.setStyle("-fx-background-color: #ff0000; "))
                // todo: Lookup if there is a unit at this coordinate
                selectedTile = Some(newCoordinate)
            }
            case (Some(coordinate), newCoordinate) if coordinate == newCoordinate => {
                board
                    .getChildren
                    .filtered(button => buttonByCoordinate(button, coordinate))
                    .forEach(button => button.setStyle(""))
                selectedTile = None
            }
            case (Some(coordinate), newCoordinate) => {
                board
                    .getChildren
                    .filtered(button => buttonByCoordinate(button, coordinate) || buttonByCoordinate(button, newCoordinate))
                    .forEach(button => button.setStyle(""))
                controller.inputCommand(Move(coordinate, newCoordinate))
                selectedTile = None
            }
        }
    }

    def buttonByCoordinate(button: Node, coordinate: Coordinate) = {
        (JGridPane.getRowIndex(button) == coordinate.y && JGridPane.getColumnIndex(button) == coordinate.x)
    }
}
