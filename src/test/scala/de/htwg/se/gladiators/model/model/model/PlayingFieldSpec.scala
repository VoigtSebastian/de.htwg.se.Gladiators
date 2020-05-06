package de.htwg.se.gladiators.model.model.model

import de.htwg.se.gladiators.model._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.gladiators.controller.controllerComponent.MoveType
import de.htwg.se.gladiators.controller.controllerComponent.MoveType.MoveType
import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.gladiators.util.Coordinate

class PlayingFieldSpec extends WordSpec with Matchers {
    "The Playing Field" when {
        "new" should {
            val playingField = createPlayingField()
            print(playingField.formatLine(0))
            "have a nice String representation" in {
                playingField.formatLine(0) should be("201")
                playingField.formatLine(1) should be("0S1")
                playingField.formatLine(2) should be("002")
            }
        }

        "told to perform an attack" should {
            "using player two" in {
                var playingField = createPlayingField()
                val gladP1 = playingField.gladiatorPlayer1.head
                val gladP2 = playingField.gladiatorPlayer2.head

                val hpAfterAttack = (gladP2.hp - gladP1.ap).toInt

                playingField = playingField.attack(gladP1, gladP2)
                playingField.gladiatorPlayer2.head.hp.toInt should be (hpAfterAttack)
            }
            "using player one" in {
                var playingField = createPlayingField()
                val gladP1 = playingField.gladiatorPlayer1.head
                val gladP2 = playingField.gladiatorPlayer2.head

                val hpAfterAttack = (gladP1.hp - gladP2.ap).toInt

                playingField = playingField.attack(gladP2, gladP1)
                playingField.gladiatorPlayer1.head.hp.toInt should be (hpAfterAttack)
            }
        }

        "categorizing moves" should {
            "return already moved" in {
                val playingField = createPlayingField()
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(0, 1), playingField.gladiatorPlayer2.head.player) should be (MoveType.ALREADY_MOVED)
            }
            "return move out of bounds" in {
                val playingField = createPlayingField()
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(-1, -1), playingField.gladiatorPlayer2.head.player) should be (MoveType.MOVE_OUT_OF_BOUNDS)
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(1, playingField.size), playingField.gladiatorPlayer2.head.player) should be (MoveType.MOVE_OUT_OF_BOUNDS)
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(playingField.size, 1), playingField.gladiatorPlayer2.head.player) should be (MoveType.MOVE_OUT_OF_BOUNDS)

            }
        }
    }

    def createPlayingField(): PlayingField = {
        val cells = Array.ofDim[Cell](3, 3)
        cells(0)(0) = Cell(CellType.BASE)
        cells(0)(1) = Cell(CellType.SAND)
        cells(0)(2) = Cell(CellType.PALM)
        cells(1)(0) = Cell(CellType.SAND)
        cells(1)(1) = Cell(CellType.SAND)
        cells(1)(2) = Cell(CellType.PALM)
        cells(2)(0) = Cell(CellType.SAND)
        cells(2)(1) = Cell(CellType.SAND)
        cells(2)(2) = Cell(CellType.BASE)

        val SAND_BACKGROUND = "\033[103m"
        val PALM_BACKGROUND = "\033[43m"
        val BASE_BACKGROUND = "\033[101m"
        val UNIT_BACKGROUND = "\033[45m"
        val TEXT_COLOR_BLACK = "\33[97m"
        val RESET_ANSI_ESCAPE = "\033[0m"

        var playingField = PlayingField()
        playingField = playingField.updateCells(cells)

        var glad1 = GladiatorFactory.createGladiator(0, 0, GladiatorType.SWORD, Player())
        var glad2 = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, Player())

        playingField = playingField.addGladPlayerOne(glad1)
        playingField = playingField.addGladPlayerTwo(glad2)

        playingField
    }
}
