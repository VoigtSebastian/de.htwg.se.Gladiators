package de.htwg.se.gladiators.model.playerService

import scalaj.http.Http
import play.api.libs.json._
import PlayerServiceReturnTypesJson._
import scala.util.Success
import scala.util.Failure
import scala.util.Try

object PlayerServiceRequests {
    def queryPlayerByName(name: String): Option[Either[PlayerServiceReturnTypes.Player, PlayerServiceReturnTypes.Error]] = {
        val body = Try(
            Json.parse(
                Http(f"http://localhost:5050/player/name/$name")
                    .asString
                    .body)) match {
                case Success(value) => value
                case Failure(_) => return None
            }
        Json
            .fromJson[PlayerServiceReturnTypes.Player](body)
            .asOpt match {
                case Some(player) => return Some(Left(player))
                case None => Json
                    .fromJson[PlayerServiceReturnTypes.Error](body)
                    .asOpt match {
                        case Some(error) => return Some(Right(error))
                        case None => return None
                    }
            }
    }
    def playerPlayed(name: String, won: Boolean): Option[Either[PlayerServiceReturnTypes.Player, PlayerServiceReturnTypes.Error]] = {
        val body = Try(
            Json.parse(
                Http(f"""http://localhost:5050/player/${if (won) "won" else "played"}/$name""")
                    .postForm
                    .asString
                    .body)) match {
                case Success(value) => value
                case Failure(_) => return None
            }
        Json
            .fromJson[PlayerServiceReturnTypes.Player](body)
            .asOpt match {
                case Some(player) => return Some(Left(player))
                case None => Json
                    .fromJson[PlayerServiceReturnTypes.Error](body)
                    .asOpt match {
                        case Some(error) => return Some(Right(error))
                        case None => return None
                    }
            }
    }
}
