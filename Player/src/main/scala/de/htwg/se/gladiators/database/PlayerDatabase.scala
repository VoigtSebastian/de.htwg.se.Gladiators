package de.htwg.se.gladiators.database

import de.htwg.se.gladiators.model.Player
trait PlayerDatabase {

    def createPlayer(player: Player): Option[Player]

    def readPlayers(): Seq[(String, Int)]

    def updatePlayer(player: Player): Option[Player]

    def deletePlayer(player: Player): Option[Player]

}