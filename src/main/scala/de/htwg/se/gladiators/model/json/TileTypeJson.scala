package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.TileType

import play.api.libs.json._

object TileTypeJson {
    implicit val tileTypeWriters = new Writes[TileType] {
        def writes(tile: TileType) = tile.toJsObject
    }
}
