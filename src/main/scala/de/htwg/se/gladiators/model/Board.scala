package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import scala.concurrent.{ Future, ExecutionContext }
import scala.concurrent.Await
import scala.concurrent.duration.{ Duration, SECONDS }

case class Board(tiles: Vector[Vector[TileType]]) {
    implicit val ec = ExecutionContext.global

    def isCoordinateLegal(coordinate: Coordinate) = (coordinate.x >= 0 && coordinate.x < tiles.length && coordinate.y >= 0 && coordinate.y < tiles.length)

    def tileAtCoordinate(coordinate: Coordinate) = tiles(coordinate.y)(coordinate.x)

    def getValidCoordinates(currentPosition: Coordinate, movementPoints: Int, validTileTypes: Vector[TileType]) = Await.result(
        getValidCoordinatesFuture(currentPosition, movementPoints, validTileTypes),
        Duration(5, SECONDS))

    def getValidCoordinatesFuture(currentPosition: Coordinate, movementPoints: Int, validTileTypes: Vector[TileType]): Future[List[Coordinate]] = {
        if (movementPoints < 0 || !isCoordinateLegal(currentPosition) || !validTileTypes.contains(tileAtCoordinate(currentPosition)))
            return Future(List())

        val thisPosition = List(currentPosition)

        for (
            right <- getValidCoordinatesFuture(currentPosition.copy(x = (currentPosition.x + 1)), movementPoints - 1, validTileTypes);
            left <- getValidCoordinatesFuture(currentPosition.copy(x = (currentPosition.x - 1)), movementPoints - 1, validTileTypes);
            up <- getValidCoordinatesFuture(currentPosition.copy(y = (currentPosition.y + 1)), movementPoints - 1, validTileTypes);
            down <- getValidCoordinatesFuture(currentPosition.copy(y = (currentPosition.y - 1)), movementPoints - 1, validTileTypes)
        ) yield (right ::: left ::: up ::: down ::: thisPosition).distinct
    }
}
