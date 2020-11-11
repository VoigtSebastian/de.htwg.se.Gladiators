package de.htwg.se.gladiators.util.Factories

import de.htwg.se.gladiators.model.Board
import de.htwg.se.gladiators.model.TileType

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardFactorySpec extends AnyWordSpec with Matchers {
    "A BoardFactory" when {
        "creating a board" should {
            "add a base to an existing board" in {
                val board = BoardFactory
                    .addBase(createSandBoard.tiles, createSandBoard.tiles.length)
                board(0)(1) should be(TileType.Base)
                board(2)(1) should be(TileType.Base)
            }
            "add a mine to an existing board" in {
                val board = BoardFactory
                    .addMine(createSandBoard.tiles, createSandBoard.tiles.length)
                board(1).filter(_.isInstanceOf[TileType.Mine]).length > 0 should be(true)
            }
            "combine everything into one" in {
                val board = BoardFactory.initRandomBoard(3)
                board.tiles(0)(1) should be(TileType.Base)
                board.tiles(2)(1) should be(TileType.Base)
                board.tiles(1).filter(_.isInstanceOf[TileType.Mine]).length > 0 should be(true)
            }
        }
    }

    def createSandBoard: Board = {
        val tiles = Vector(
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand),
            Vector(TileType.Sand, TileType.Sand, TileType.Sand))
        Board(tiles)
    }
}
