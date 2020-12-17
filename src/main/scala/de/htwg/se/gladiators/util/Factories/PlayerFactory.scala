package de.htwg.se.gladiators.util.Factories

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.model.Gladiator

object PlayerFactory {
    def apply(
        id: Int = 0,
        name: String = "",
        enemyBaseLine: Int = 0,
        credits: Int = 0,
        health: Int = 0,
        alreadyBought: Boolean = true,
        gamesPlayed: Option[Int] = None,
        gamesWon: Option[Int] = None,
        gladiators: Vector[Gladiator] = Vector()): Player = {
        return Player(
            id,
            name,
            enemyBaseLine,
            credits,
            health,
            alreadyBought,
            gamesPlayed,
            gamesWon,
            gladiators)
    }
}
