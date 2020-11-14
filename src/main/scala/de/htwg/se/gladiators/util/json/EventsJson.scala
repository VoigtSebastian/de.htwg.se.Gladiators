package de.htwg.se.gladiators.util.json

import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.model.json.PlayerJson._
import de.htwg.se.gladiators.model.json.GladiatorJson._
import de.htwg.se.gladiators.util.json.CoordinateJson._

import play.api.libs.json._

object EventsJson {
    implicit val eventsWrites = new Writes[Events] {
        def writes(event: Events) = event match {
            case Init => Json.obj("type" -> "Init")
            case Shutdown => Json.obj("type" -> "Shutdown")
            case PlayerOneNamed(name) => Json.obj(
                "type" -> "PlayerOneNamed",
                "name" -> name)
            case PlayerTwoNamed(name) => Json.obj(
                "type" -> "PlayerTwoNamed",
                "name" -> name)
            case SuccessfullyBoughtGladiator(player, gladiator) => Json.obj(
                "type" -> "SuccessfullyBoughtGladiator",
                "player" -> player,
                "gladiator" -> gladiator)
            case Turn(player) => Json.obj(
                "type" -> "Turn",
                "player" -> player)
            case Moved(player, from, to, gladiator) => Json.obj(
                "type" -> "Moved",
                "player" -> player,
                "from" -> from,
                "to" -> to,
                "gladiator" -> gladiator)
            case Attacked(currentPlayer, killed, from, to) => Json.obj(
                "type" -> "Attacked",
                "currentPlayer" -> currentPlayer,
                "killed" -> killed,
                "from" -> from,
                "to" -> to)
            case BaseAttacked(currentPlayer) => Json.obj(
                "type" -> "BaseAttacked",
                "currentPlayer" -> currentPlayer)
            case Mined(currentPlayer, amount, depleted) => Json.obj(
                "type" -> "Mined",
                "currentPlayer" -> currentPlayer,
                "amount" -> amount,
                "depleted" -> depleted)
            case Won(player) => Json.obj(
                "type" -> "Won",
                "player" -> player)
            case ErrorMessage(message) => Json.obj(
                "type" -> "ErrorMessage",
                "message" -> message)
        }
    }
}
