package de.htwg.se.gladiators.util.Factories

import scala.util.Random

import de.htwg.se.gladiators.model.Board
import de.htwg.se.gladiators.model.TileType

object BoardFactory {
    def initRandomBoard(dimensions: Int = 15, percentageSand: Int = 80) = Board(
        addMine(
            addBase(
                (1 to dimensions)
                    .map(_ => (1 to dimensions).map(_ =>
                        if (Random.nextInt(100) < percentageSand)
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

    /**
     * Sand Sand Sand
     * Sand Sand Sand
     * Sand Sand Sand
     * @return Board consisting only of Sand-tiles
     */
    def createSandBoard3x3: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand))
        Board(tiles)
    }

    /**
     * Sand Base Palm
     * Sand Sand Sand
     * Palm Base Sand
     * @return Board containing Sand-, Palm- and Base-tiles
     */
    def createNormalBoard3x3: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Base, TileType.Palm),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Palm, TileType.Base, TileType.Sand))
        Board(tiles)
    }
}
