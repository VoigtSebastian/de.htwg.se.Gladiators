package de.htwg.se.gladiators.database.relational

import de.htwg.se.gladiators.database.PlayerDataBase

import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

object RelationalDb extends PlayerDatabase {
    private val mappings: Mappings.type = Mappings

    def createPlayer(desk: DeskInterface): Option[DeskInterface] = {
        try {
            val worked = mappings.createPlayer(desk.players.head)
            if (worked) {
                println("Saved player in database")
                Some(desk)
            } else {
                println("Error saving player in database")
                None
            }
        } catch {
            case _: Throwable => None
        }
    }

    def readPlayer(): Option[PlayerInterface] = {
        try {
            mappings.readPlayer()
        } catch {
            case _: Throwable => None
        }
    }

}