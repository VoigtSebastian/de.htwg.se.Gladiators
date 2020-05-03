package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType._


case class Gladiator(line: Int, row: Int, movementPoints: Double, ap: Double, hp: Double, gladiatorType: GladiatorType, player: Player, moved: Boolean = true) {

    def move(line: Int, row: Int): Gladiator = {
        this.copy(line = line, row = row)
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
