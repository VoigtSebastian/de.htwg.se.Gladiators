package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.MovementType
import de.htwg.se.gladiators.util.MovementType._
import de.htwg.se.gladiators.model.TileType._

object Moves {
    def movementType(from: Coordinate, to: Coordinate, board: Board, currentPlayer: Player, enemyPlayer: Player): MovementType = {
        checkGladiatorOwnership(from, to, currentPlayer, enemyPlayer) match {
            case Some(value) => return value
            case None => ()
        }
        if (!board.isCoordinateLegal(from) || !board.isCoordinateLegal(to))
            return MoveOutOfBounds
        (board.tileAtCoordinate(from), board.tileAtCoordinate(to)) match {
            case (_, Palm) => MoveToPalm
            case (_, Base) => checkBaseAttack(from, currentPlayer, board) match {
                case true => BaseAttack
                case false => IllegalMove
            }
            case (_, Sand) => checkLegalMove(from, to, currentPlayer, board) match {
                case true => enemyPlayer.gladiators.filter(_.position == to).length > 0 match {
                    case true => Attack
                    case false => Move
                }
                case false => IllegalMove
            }
            case _ => ???
        }
    }

    def checkGladiatorOwnership(from: Coordinate, to: Coordinate, currentPlayer: Player, enemyPlayer: Player): Option[MovementType] = {
        if (!(enemyPlayer.gladiators ++ currentPlayer.gladiators).map(_.position).contains(from)) {
            return Some(NoUnitAtCoordinate)
        }
        if (currentPlayer.gladiators.map(_.position).contains(to)) {
            return Some(TileBlocked)
        }
        if (enemyPlayer.gladiators.map(_.position).contains(from)) {
            return Some(NotOwnedByPlayer)
        }
        if (currentPlayer.gladiators.filter(_.position == from).head.moved) {
            return Some(AlreadyMoved)
        }
        None
    }

    def checkBaseAttack(from: Coordinate, currentPlayer: Player, board: Board): Boolean = {
        (board.getValidCoordinates(
            from,
            currentPlayer
                .gladiators
                .filter(_.position == from)(0)
                .movementPoints,
            Vector(Sand, Base))
            .filter(coordinate => (coordinate.x == board.tiles.length / 2) && (coordinate.y == currentPlayer.enemyBaseLine))
            .length >= 1)
    }

    def checkLegalMove(from: Coordinate, to: Coordinate, currentPlayer: Player, board: Board): Boolean = {
        (board.getValidCoordinates(
            from,
            currentPlayer
                .gladiators
                .filter(_.position == from)(0)
                .movementPoints,
            Vector(Sand))
            .contains(to))
    }
}
