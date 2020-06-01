package de.htwg.se.gladiators.playerModule.util

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

case class UpdateNameArgumentContainer(player: PlayerInterface, name: String)

object UpdateNameArgumentContainer  extends PlayJsonSupport  {
  import play.api.libs.json._
  implicit val containerWrites: OWrites[UpdateNameArgumentContainer] = Json.writes[UpdateNameArgumentContainer]
  implicit val containerReads: Reads[UpdateNameArgumentContainer] = Json.reads[UpdateNameArgumentContainer]
}
