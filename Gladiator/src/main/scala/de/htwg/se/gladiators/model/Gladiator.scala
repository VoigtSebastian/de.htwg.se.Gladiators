package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface


case class Gladiator(line: Int, row: Int, movementPoints: Double, ap: Double, hp: Double, gladiatorType: GladiatorType, player: PlayerInterface, moved: Boolean = true) {

    def move(line: Int, row: Int): Gladiator = {
        this.copy(line = line, row = row, moved = true)
    }

    def updateMoved(moved: Boolean): Gladiator = {
        this.copy(moved = moved)
    }

    def getAttacked(ap: Double): Gladiator = {
        this.copy(hp = hp - ap)
    }

    def assignPlayer(player: PlayerInterface): Gladiator = {
        this.copy(player = player)
    }

    def !=(g: Gladiator): Boolean = {
        line != g.line || row != g.row || hp != g.hp || movementPoints != g.movementPoints || ap != g.ap
    }

    override def toString: String = {
        "The gladiator in line " + line + " and row " + row +
            " has " + movementPoints + " movement points, " +
            ap + " attack points, " +
            hp + " health points, has the type of " +
            gladiatorType.toString +
            " and is owned by " + player.name
    }
}
