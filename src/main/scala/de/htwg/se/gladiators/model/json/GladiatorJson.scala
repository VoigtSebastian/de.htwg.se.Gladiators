package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.util.json.CoordinateJson._

import play.api.libs.json._

object GladiatorJson {
    implicit val gladiatorWriters = new Writes[Gladiator] {
        def writes(gladiator: Gladiator) = Json.obj(
            "gladiatorType" -> gladiator.gladiatorType.simpleString,
            "position" -> gladiator.position,
            "healthPoints" -> gladiator.healthPoints,
            "initialHealthPoints" -> gladiator.initialHealth,
            "movementPoints" -> gladiator.movementPoints,
            "movementPointsAttack" -> gladiator.gladiatorType.movementPointsAttack,
            "attackPoints" -> gladiator.attackPoints,
            "moved" -> gladiator.moved,
            "cost" -> gladiator.cost)
    }
}
