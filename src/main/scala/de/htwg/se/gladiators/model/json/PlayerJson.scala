package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.model.json.GladiatorJson._

import play.api.libs.json._

object PlayerJson {
    implicit val playerWrites = new Writes[Player] {
        def writes(player: Player) = Json.obj(
            "name" -> player.name,
            "enemyBaseLine" -> player.enemyBaseLine,
            "credits" -> player.credits,
            "health" -> player.health,
            "alreadyBought" -> player.alreadyBought,
            "gladiators" -> player.gladiators)
    }
}
