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
            case Init => Json.obj("eventType" -> "Init")
            case Shutdown => Json.obj("eventType" -> "Shutdown")
            case PlayerOneNamed(name) => Json.obj(
                "eventType" -> "PlayerOneNamed",
                "name" -> name)
            case PlayerTwoNamed(name) => Json.obj(
                "eventType" -> "PlayerTwoNamed",
                "name" -> name)
            case SuccessfullyBoughtGladiator(player, gladiator) => Json.obj(
                "eventType" -> "SuccessfullyBoughtGladiator",
                "player" -> player,
                "gladiator" -> gladiator)
            case Turn(player) => Json.obj(
                "eventType" -> "Turn",
                "player" -> player)
            case Moved(player, from, to, gladiator) => Json.obj(
                "eventType" -> "Moved",
                "player" -> player,
                "from" -> from,
                "to" -> to,
                "gladiator" -> gladiator)
            case Attacked(currentPlayer, killed, from, to) => Json.obj(
                "eventType" -> "Attacked",
                "currentPlayer" -> currentPlayer,
                "killed" -> killed,
                "from" -> from,
                "to" -> to)
            case BaseAttacked(currentPlayer) => Json.obj(
                "eventType" -> "BaseAttacked",
                "currentPlayer" -> currentPlayer)
            case Mined(currentPlayer, amount, depleted) => Json.obj(
                "eventType" -> "Mined",
                "currentPlayer" -> currentPlayer,
                "amount" -> amount,
                "depleted" -> depleted)
            case Won(player) => Json.obj(
                "eventType" -> "Won",
                "player" -> player)
            case ErrorMessage(message) => Json.obj(
                "eventType" -> "ErrorMessage",
                "message" -> message)
        }
    }
}
