package de.htwg.se.gladiators.database.relational

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.database.PlayerDatabase
import scala.util.{ Try, Success, Failure }

object SlickDatabase extends PlayerDatabase {
    private val mappings: Mappings.type = Mappings

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

    def readPlayer(): Option[Player] = {
        try {
            mappings.readPlayer()
        } catch {
            case _: Throwable => None
        }
    }

    def updatePlayer(player: Player): Option[Player] = {
        Some(player)
    }

    def deletePlayer(player: Player): Option[Player] = {
        Some(player)
    }

}