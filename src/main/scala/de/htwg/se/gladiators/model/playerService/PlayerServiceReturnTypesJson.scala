package de.htwg.se.gladiators.model.playerService

import de.htwg.se.gladiators.model.playerService.PlayerServiceReturnTypes.{ Player, Error }

import play.api.libs.functional.syntax._
import play.api.libs.json._

object PlayerServiceReturnTypesJson {
    implicit val playerWrites = new Writes[Player] {
        def writes(player: Player) = Json.obj(
            "id" -> player.id,
            "player_name" -> player.player_name,
            "games_played" -> player.games_played,
            "games_won" -> player.games_won)
    }
    implicit val playerReads: Reads[Player] = (
        (JsPath \ "id").read[Int] and
        (JsPath \ "player_name").read[String] and
        (JsPath \ "games_played").read[Int] and
        (JsPath \ "games_won").read[Int])(Player.apply _)

    implicit val errorWrites = new Writes[Error] {
        def writes(error: Error) = Json.obj(
            "error_type" -> error.error_type,
            "message" -> error.message)
    }
    implicit val errorReads: Reads[Error] = (
        (JsPath \ "error_type").read[String] and
        (JsPath \ "message").read[String])(Error.apply _)
}
