package de.htwg.se.gladiators.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import de.htwg.se.gladiators.controller.json.GameStateJson._

class GameStateSpec extends AnyWordSpec with Matchers {
    "Every GameState" should {
        "be serializable to json" in {
            GameState
                .values
                .foreach(gs => Json.toJson(gs).toString should not be (empty))
            GameState
                .values
                .foreach(gs => Json.toJson(gs).toString.drop(1).dropRight(1) should be(gs.entryName))
        }
    }
}
