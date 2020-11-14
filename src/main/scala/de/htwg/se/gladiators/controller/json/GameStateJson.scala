package de.htwg.se.gladiators.controller.json

import play.api.libs.json._
import de.htwg.se.gladiators.controller.GameState
import de.htwg.se.gladiators.controller.GameState._

object GameStateJson {
    implicit val gameStateWrites = new Writes[GameState] {
        def writes(gameState: GameState) = gameState match {
            case NamingPlayerOne => JsString("NamingPlayerOne")
            case NamingPlayerTwo => JsString("NamingPlayerTwo")
            case TurnPlayerOne => JsString("TurnPlayerOne")
            case TurnPlayerTwo => JsString("TurnPlayerTwo")
            case Finished => JsString("Finished")
        }
    }
}
