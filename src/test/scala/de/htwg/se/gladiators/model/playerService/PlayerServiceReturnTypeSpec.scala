package de.htwg.se.gladiators.model.playerService

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._
import PlayerServiceReturnTypesJson._

class PlayerServiceReturnTypeSpec extends AnyWordSpec with Matchers {
    "A Player from the Player Service" when {
        "parsed to json" should {
            val player: JsValue = Json.toJson(PlayerServiceReturnTypes.Player(0, "name", 0, 0))
            "have the correct values" in {
                (player \ "id").validate[Int].get should be(0)
                (player \ "player_name").validate[String].get should be("name")
                (player \ "games_played").validate[Int].get should be(0)
                (player \ "games_won").validate[Int].get should be(0)
            }
        }
        "parsed from json" should {
            "not fail" in {
                val strPlayer = """{ "id": 0, "player_name": "name", "games_played": 0, "games_won": 0 }"""
                val player = Json.fromJson[PlayerServiceReturnTypes.Player](Json.parse(strPlayer)).get
                player.id should be(0)
                player.player_name should be("name")
                player.games_played should be(0)
                player.games_won should be(0)
            }
        }
    }
    "An Error from the Player Service" when {
        "parsed to json" should {
            val error: JsValue = Json.toJson(PlayerServiceReturnTypes.Error("ErrorType", "Message"))
            "have the correct values" in {
                (error \ "error_type").validate[String].get should be("ErrorType")
                (error \ "message").validate[String].get should be("Message")
            }
        }
        "parsed from json" should {
            "not fail" in {
                val strError = """{ "error_type": "error", "message": "message"}"""
                val error = Json.fromJson[PlayerServiceReturnTypes.Error](Json.parse(strError)).get
                error.error_type should be("error")
                error.message should be("message")
            }
        }
    }
}
