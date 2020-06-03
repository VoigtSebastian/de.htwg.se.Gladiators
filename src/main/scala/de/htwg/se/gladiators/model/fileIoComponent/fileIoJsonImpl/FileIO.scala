package de.htwg.se.gladiators.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.gladiators.GladiatorsModule
import de.htwg.se.gladiators.controller.controllerComponent.ControllerInterface
import de.htwg.se.gladiators.model.{ Cell, CellType }
import de.htwg.se.gladiators.model.CellType.CellType
import de.htwg.se.gladiators.model.fileIoComponent.FileIOInterface
import de.htwg.se.gladiators.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import play.api.libs.json._

import scala.io.Source

class FileIO extends FileIOInterface {

    override def load: PlayingFieldInterface = {
        // var playingField: PlayingFieldInterface = null
        val source: String = Source.fromFile("playingfield.json").getLines.mkString
        val json: JsValue = Json.parse(source)
        val size = (json \ "playingfield" \ "size").get.toString.toInt
        val injector = Guice.createInjector(new GladiatorsModule)
        val playingField = injector.getInstance((classOf[PlayingFieldInterface]))

        for (index <- 0 until size * size) {
            val line = (json \\ "line")(index).as[Int]
            val row = (json \\ "row")(index).as[Int]
            var cell = (json \\ "cell")(index).as[Int]
            var cellType: CellType = CellType.SAND
            cell match {
                case 0 => cellType = CellType.SAND
                case 1 => cellType = CellType.PALM
                case 2 => cellType = CellType.BASE
                case 3 => cellType = CellType.GOLD
                case _ =>
            }

            playingField.setCell(line, row, cellType)
        }
        playingField
    }

    override def save(playingField: PlayingFieldInterface): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("playingfield.json"))
        pw.write(Json.prettyPrint(fieldToJson(playingField)))
        pw.close
    }

    def fieldToJson(playingField: PlayingFieldInterface) = {
        Json.obj(
            "playingfield" -> Json.obj(
                "size" -> JsNumber(playingField.cells.length),
                "cells" -> Json.toJson(
                    for {
                        line <- 0 until playingField.cells.length
                        row <- 0 until playingField.cells.length
                    } yield {
                        Json.obj(
                            "line" -> line,
                            "row" -> row,
                            "cell" -> Json.toJson(playingField.cell(line, row).cellType.id))
                    })))
    }

}
