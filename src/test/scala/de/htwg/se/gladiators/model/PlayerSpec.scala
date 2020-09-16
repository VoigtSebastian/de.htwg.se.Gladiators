package de.htwg.se.gladiators.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.util.Factories.BoardFactory
import de.htwg.se.gladiators.util.Coordinate

class PlayerSpec extends AnyWordSpec with Matchers {
    "A Player" should {
        val board = BoardFactory.initRandomBoard(7, 100)
        val playerOne = Player("", 6, 0, Vector())
        val playerTwo = Player("", 0, 0, Vector())
        "return all tiles around the own base at the bottom of the map" in {
            val tiles = playerOne
                .placementTilesNewUnit(7, board.tiles)

            tiles should contain(Coordinate(2, 0))
            tiles should contain(Coordinate(4, 0))
            tiles should contain(Coordinate(3, 1))
        }
        "return all tiles around the own base at the top of the map" in {
            val tiles = playerTwo
                .placementTilesNewUnit(7, board.tiles)

            tiles should contain(Coordinate(2, 6))
            tiles should contain(Coordinate(4, 6))
            tiles should contain(Coordinate(3, 5))
        }
        "return no tiles" in {
            val palmBoard = BoardFactory.initRandomBoard(7, 0)
            playerOne.placementTilesNewUnit(7, palmBoard.tiles) should be(empty)
            playerTwo.placementTilesNewUnit(7, palmBoard.tiles) should be(empty)
        }
    }
}
