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

    def uncoloredString(gladiators: Vector[Gladiator]): String = {
        "   " + (0 to (tiles.length - 1)).map(n => f"${n}%2d ").mkString + "\n" + tiles
            .zipWithIndex
            .map({
                case (rows, y) => f"$y%2d " + rows.zipWithIndex.map({
                    case (tile, x) =>
                        " " + tile.stringRepresentation(gladiatorAtPosition(gladiators, Coordinate(x, y))) + " "
                }).mkString + "\n"
            }).mkString
    }

    def coloredString(gladiators: Vector[Gladiator]): String = {
        "   " + (0 to (tiles.length - 1)).map(n => f"${n}%2d ").mkString + "\n" + tiles
            .zipWithIndex
            .map({
                case (rows, y) => f"$y%2d " + rows.zipWithIndex.map({
                    case (tile, x) =>
                        tile.coloredStringRepresentation(gladiatorAtPosition(gladiators, Coordinate(x, y)))
                }).mkString + "\n"
            }).mkString

    }

    def gladiatorAtPosition(gladiators: Vector[Gladiator], coordinate: Coordinate): Option[GladiatorType] =
        gladiators
            .find(_.position == coordinate) match {
                case Some(value) => Some(value.gladiatorType)
                case None => None
            }

    def updateTile(position: Coordinate, newTile: TileType) = this.copy(tiles.updated(position.y, tiles(position.y).updated(position.x, newTile)))
}
