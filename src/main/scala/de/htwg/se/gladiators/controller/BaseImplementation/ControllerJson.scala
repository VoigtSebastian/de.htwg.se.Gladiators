package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.model.json.BoardJson._
import de.htwg.se.gladiators.model.json.PlayerJson._
import de.htwg.se.gladiators.model.json.ShopJson._
import de.htwg.se.gladiators.controller.json.GameStateJson._

import play.api.libs.json._

object ControllerJson {
    implicit val controllerWrites = new Writes[Controller] {
        def writes(controller: Controller) = Json.obj(
            "gameState" -> controller.gameState,
            "playerOne" -> controller.playerOne,
            "playerTwo" -> controller.playerTwo,
            "board" -> controller.board,
            "shop" -> controller.shop,
            "currentPlayer" -> controller.currentPlayer)
    }
}
