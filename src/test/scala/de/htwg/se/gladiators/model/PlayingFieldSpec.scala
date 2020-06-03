package de.htwg.se.gladiators.model

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
                playingField.formatLine(0) should be("2S1")
                playingField.formatLine(1) should be("0S1")
                playingField.formatLine(2) should be("002")
            }
        }

        "told to perform an attack" should {
            "use player two" in {
                var playingField = createPlayingField()
                val gladP1 = playingField.gladiatorPlayer1.head
                val gladP2 = playingField.gladiatorPlayer2.head

                val hpAfterAttack = (gladP2.hp - gladP1.ap).toInt

                playingField = playingField.attack(gladP1, gladP2)
                playingField.gladiatorPlayer2.head.hp.toInt should be (hpAfterAttack)
            }
            "use player one" in {
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
                val playerTwo = playingField.gladiatorPlayer2.head.player
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(-1, -1), playerTwo) should be (MoveType.MOVE_OUT_OF_BOUNDS)
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(1, playingField.size), playerTwo) should be (MoveType.MOVE_OUT_OF_BOUNDS)
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(playingField.size, 1), playerTwo) should be (MoveType.MOVE_OUT_OF_BOUNDS)

            }
            "return base attack" in {
                val playingField = createPlayingField().resetGladiatorMoved()
                val playerOne = playingField.gladiatorPlayer1.head.player
                playingField.checkMoveType(Coordinate(0, 1), Coordinate(0, 0), playerOne) should be (MoveType.BASE_ATTACK)
            }
            "return unit not existing" in {
                val playingField = createPlayingField()
                playingField.checkMoveType(Coordinate(0, 0), Coordinate(0, 1), Player()) should be (MoveType.UNIT_NOT_EXISTING)
            }
            "return unit not owned by player" in {
                val playingField = createPlayingField().resetGladiatorMoved()
                val playerOne = playingField.gladiatorPlayer1.head.player
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(1, 0), playerOne) should be (MoveType.UNIT_NOT_OWNED_BY_PLAYER)
            }
            "return move to palm" in {
                val playingField = createPlayingField().resetGladiatorMoved()
                val playerTwo = playingField.gladiatorPlayer2.head.player
                playingField.checkMoveType(Coordinate(1, 1), Coordinate(0, 2), playerTwo) should be (MoveType.MOVE_TO_PALM)
            }
        }
        "setting Gladiators" should {
            "return an updated playingField for player one" in {
                val playingField = createPlayingField()
                val updatedPlayingField = playingField.setGladiator(0, 1, GladiatorFactory.createGladiator(0, 1, GladiatorType.BOW, playingField.gladiatorPlayer1.head.player))
                updatedPlayingField.gladiatorPlayer1.head.gladiatorType should be (GladiatorType.BOW)
            }
            "return an updated playingField for player two" in {
                val playingField = createPlayingField()
                val updatedPlayingField = playingField.setGladiator(1, 1, GladiatorFactory.createGladiator(1, 1, GladiatorType.TANK, playingField.gladiatorPlayer2.head.player))
                updatedPlayingField.gladiatorPlayer2.head.gladiatorType should be (GladiatorType.TANK)
            }
        }
        "getting asked for Gladiator Information" should {
            "return an empty string" in {
                val playingField = createPlayingField()
                playingField.gladiatorInfo(0, 0) should fullyMatch regex ""
            }
            "return a string representation of the gladiators of player one" in {
                val playingField = createPlayingField()
                playingField.gladiatorInfo(0, 1) should fullyMatch regex playingField.gladiatorPlayer1.head.toString()
            }
            "return a string representation of the gladiators of player two" in {
                val playingField = createPlayingField()
                playingField.gladiatorInfo(1, 1) should fullyMatch regex playingField.gladiatorPlayer2.head.toString()
            }
        }
        "moving gladiators" should {
            "return an updated playingField for Player One" in {
                val playingField = createPlayingField()
                playingField.moveGladiator(0, 1, 1, 1).gladiatorPlayer1.head.line should be (1)
                playingField.moveGladiator(0, 1, 2, 0).gladiatorPlayer1.head.row should be (0)
            }
            "return an updated playingField for Player Two" in {
                val playingField = createPlayingField()
                playingField.moveGladiator(1, 1, 1, 2).gladiatorPlayer2.head.row should be (2)
                playingField.moveGladiator(1, 1, 2, 0).gladiatorPlayer2.head.line should be (2)
            }
            "determine all valid coordinates to move to" in {
                val playingField = createPlayingField()
                val newGladiator = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, playingField.gladiatorPlayer2.head.player)
                val updatedPlayingField = playingField.setGladiator(1, 1, newGladiator)
                playingField.getValidMoveCoordinates(newGladiator, Coordinate(1, 1)).length should be (3)
            }
            "check if a destination is a valid move coordination" in {
                val playingField = createPlayingField()
                val newGladiator = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, playingField.gladiatorPlayer2.head.player)
                val updatedPlayingField = playingField.setGladiator(1, 1, newGladiator)
                playingField.checkMovementPointsMove(newGladiator, Coordinate(1, 1), Coordinate(2,0)) should be (true)
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
        /*
          0 1 2
        0 B S P
        1 S S P
        2 S S B
        */

        val SAND_BACKGROUND = "\u001b[103m"
        val PALM_BACKGROUND = "\u001b[43m"
        val BASE_BACKGROUND = "\u001b[101m"
        val UNIT_BACKGROUND = "\u001b[45m"
        val TEXT_COLOR_BLACK = "\u001b[97m"
        val RESET_ANSI_ESCAPE = "\u001b[0m"

        var playingField = PlayingField(size=3)
        playingField = playingField.updateCells(cells)

        var glad1 = GladiatorFactory.createGladiator(0, 1, GladiatorType.SWORD, Player())
        var glad2 = GladiatorFactory.createGladiator(1, 1, GladiatorType.SWORD, Player(enemyBaseLine = playingField.size - 1))


        playingField = playingField.addGladPlayerOne(glad1)
        playingField = playingField.addGladPlayerTwo(glad2)

        playingField
    }
}
