package de.htwg.se.gladiators.util

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.se.gladiators.model.Player

import spray.json._

case class UpdateNameArgumentContainer(player: Player, name: String)

object UpdateNameArgumentContainerProtocol extends DefaultJsonProtocol {
    implicit val playerFormat = jsonFormat5(Player.apply)
    implicit val shipAddressFormat = jsonFormat2(UpdateNameArgumentContainer.apply)
}
