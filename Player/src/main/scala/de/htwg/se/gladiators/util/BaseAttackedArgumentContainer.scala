package de.htwg.se.gladiators.playerModule.util

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

case class BaseAttackedArgumentContainer(player: PlayerInterface, ap: Int)

object BaseAttackedArgumentContainer extends PlayJsonSupport {
    import play.api.libs.json._
    implicit val containerWrites: OWrites[BaseAttackedArgumentContainer] = Json.writes[BaseAttackedArgumentContainer]
    implicit val containerReads: Reads[BaseAttackedArgumentContainer] = Json.reads[BaseAttackedArgumentContainer]
}
