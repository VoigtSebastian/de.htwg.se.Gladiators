package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.Board
import de.htwg.se.gladiators.model.json.TileTypeJson._

import play.api.libs.json._

object BoardJson {
    implicit val boardWriters = new Writes[Board] {
        def writes(board: Board) = Json.obj("tiles" -> board.tiles)
    }
}
