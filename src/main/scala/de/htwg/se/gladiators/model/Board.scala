package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import scala.concurrent.{ Future, ExecutionContext }
import scala.concurrent.Await
import scala.concurrent.duration.{ Duration, SECONDS }

case class Board(cells: Vector[Vector[CellType]]) {
    implicit val ec = ExecutionContext.global

    def isCoordinateLegal(coordinate: Coordinate) = (coordinate.x >= 0 && coordinate.x < cells.length && coordinate.y >= 0 && coordinate.y < cells.length)

    def cellAtCoordinate(coordinate: Coordinate) = cells(coordinate.y)(coordinate.x)


    def getValidCoordinates (currentPosition: Coordinate, movementPoints: Int, validCellTypes: Vector[CellType]) = Await.result(
        getValidCoordinatesFuture(currentPosition, movementPoints, validCellTypes),
        Duration(5, SECONDS)
    )

    def getValidCoordinatesFuture(currentPosition: Coordinate, movementPoints: Int, validCellTypes: Vector[CellType]): Future[List[Coordinate]] = {
        if (movementPoints < 0 || !isCoordinateLegal(currentPosition) || !validCellTypes.contains(cellAtCoordinate(currentPosition)))
            return Future(List())

        val thisPosition = List(currentPosition)

        for (
            right <- getValidCoordinatesFuture(currentPosition.copy(x = (currentPosition.x + 1)), movementPoints - 1, validCellTypes);
            left <- getValidCoordinatesFuture(currentPosition.copy(x = (currentPosition.x - 1)), movementPoints - 1, validCellTypes);
            up <- getValidCoordinatesFuture(currentPosition.copy(y = (currentPosition.y + 1)), movementPoints - 1, validCellTypes);
            down <- getValidCoordinatesFuture(currentPosition.copy(y = (currentPosition.y - 1)), movementPoints - 1, validCellTypes)
        ) yield (right ::: left ::: up ::: down ::: thisPosition).distinct
    }
}
