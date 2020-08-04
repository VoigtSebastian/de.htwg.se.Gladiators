package de.htwg.se.gladiators.util.Factories

import de.htwg.se.gladiators.model.Board
import de.htwg.se.gladiators.model.TileType
import scala.util.Random

object BoardFactory {
    def initRandomBoard(dimensions: Int = 15) = Board(
        addMine(
            addBase(
                (1 to dimensions)
                    .map(_ => (1 to dimensions).map(_ =>
                        if (Random.nextInt(100) < 80)
                            TileType.Sand
                        else
                            TileType.Palm)
                        .toVector)
                    .toVector,
                dimensions), dimensions))

    def addBase(vector: Vector[Vector[TileType]], dimensions: Int) =
        vector
            .updated(0, vector(0).updated(dimensions / 2, TileType.Base))
            .updated(dimensions - 1, vector(dimensions - 1).updated(dimensions / 2, TileType.Base))

    def addMine(vector: Vector[Vector[TileType]], dimensions: Int): Vector[Vector[TileType]] =
        vector
            .updated(dimensions / 2, vector(dimensions / 2).updated(Random.nextInt(dimensions), TileType.Mine(Random.nextInt(50))))
}
