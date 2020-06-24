package de.htwg.se.gladiators.database.relational

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.database.PlayerDatabase

class SlickDatabase extends PlayerDatabase {
    private val mappings: PlayerMappings.type = PlayerMappings

    def createPlayer(player: Player): Option[Player] = {
        try {
            val worked = mappings.createPlayer(player)
            if (worked) {
                println("Saved player in database")
                Some(player)
            } else {
                println("Error saving player in database")
                None
            }
        } catch {
            case _: Throwable => None
        }
    }

    def readPlayers(): Seq[(String, Int)] = {
        try {
            mappings.readPlayers()
        } catch {
            case _: Throwable => Seq()
        }
    }

    def updatePlayer(player: Player): Option[Player] = ???

    def deletePlayer(player: Player): Option[Player] = ???

}