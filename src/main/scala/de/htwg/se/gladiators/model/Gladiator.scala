package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType._


case class Gladiator(var line: Int, var row: Int, movementPoints: Double, var ap: Double, var hp: Double, gladiatorType: GladiatorType, player: Player) {

    def levelUp(value: Int): Unit = {
        this.ap += value
    }

    def move(line: Int, row: Int): Unit = {
        this.line = line
        this.row = row
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
