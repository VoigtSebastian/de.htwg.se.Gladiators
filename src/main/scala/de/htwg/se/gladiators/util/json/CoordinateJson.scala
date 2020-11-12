package de.htwg.se.gladiators.util.json

import de.htwg.se.gladiators.util.Coordinate

import play.api.libs.json._

object CoordinateJson {
    implicit val coordinateWrites = new Writes[Coordinate] {
        def writes(coordinate: Coordinate) = Json.obj(
            "x" -> coordinate.x,
            "y" -> coordinate.y)
    }
}
