package de.htwg.se.gladiators.util

import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Factories.GladiatorFactory
import de.htwg.se.gladiators.util.json.EventsJson._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import de.htwg.se.gladiators.util.Factories.PlayerFactory

class EventsSpec extends AnyWordSpec with Matchers {
    "Every Event" should {
        "have a json representation" in {
            val player = PlayerFactory()
            val coordinate = Coordinate(0, 0)
            val events: Vector[Events] = Vector(
                Init,
                PlayerOneNamed("one"),
                PlayerTwoNamed("two"),
                SuccessfullyBoughtGladiator(player, GladiatorFactory.initRandomGladiator),
                Turn(player),
                Moved(player, coordinate, coordinate, GladiatorFactory.initRandomGladiator),
                Attacked(player, false, coordinate, coordinate),
                BaseAttacked(player),
                Mined(player, 0, false),
                Shutdown,
                Won(player),
                ErrorMessage("error"))
            events.foreach(e => Json.toJson(e).toString should not be (empty))
        }
    }
}
