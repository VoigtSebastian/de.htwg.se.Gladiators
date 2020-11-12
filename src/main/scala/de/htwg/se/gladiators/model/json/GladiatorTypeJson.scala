package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.GladiatorType._

import play.api.libs.json._

object GladiatorTypeJson {
    implicit val tankWrites = new Writes[Tank.type] {
        def writes(tank: Tank.type) = JsString("Tank")
    }
    implicit val knightWrites = new Writes[Knight.type] {
        def writes(knight: Knight.type) = JsString("Knight")
    }
    implicit val archerWrites = new Writes[Archer.type] {
        def writes(archer: Archer.type) = JsString("Archer")
    }
}
