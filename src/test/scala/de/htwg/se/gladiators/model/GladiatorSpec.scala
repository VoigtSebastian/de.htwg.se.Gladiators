package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.GladiatorFactory
import de.htwg.se.gladiators.util.Factories.ShopFactory

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import de.htwg.se.gladiators.model.json.GladiatorJson._

class GladiatorSpec extends AnyWordSpec with Matchers {
    val shop = ShopFactory.initRandomShop()
    "A Gladiator" when {
        "created" should {
            "have a cost" in {
                GladiatorFactory
                    .createGladiator()
                    .cost should be > 0
            }
            "be movable" in {
                GladiatorFactory
                    .createGladiator(position = Some(Coordinate(0, 0)))
                    .move(Coordinate(1, 1))
                    .position should be(Coordinate(1, 1))
            }
            "reduce their health points when attacked by the correct amount" in {
                GladiatorFactory
                    .createGladiator(healthPoints = Some(10))
                    .attacked(10)
                    .healthPoints should be(0)
                GladiatorFactory
                    .createGladiator(healthPoints = Some(100))
                    .attacked(10)
                    .healthPoints should be(90)
                GladiatorFactory
                    .createGladiator(healthPoints = Some(0))
                    .attacked(10)
                    .healthPoints should be(-10)
            }
            "have the same cost after being copied" in {
                val initGlad = GladiatorFactory.createGladiator()
                initGlad.copy(healthPoints = 0, movementPoints = 0).cost should be(initGlad.cost)
            }
            "have the same health after being copied" in {
                val initGlad = GladiatorFactory.createGladiator()
                initGlad.copy(movementPoints = 0).initialHealth should be(initGlad.initialHealth)
            }
            "have a gladiatorType defined in its Json representation" in {
                (Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Archer))) \ "gladiatorType").as[String] should not be (empty)
                (Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Tank))) \ "gladiatorType").as[String] should not be (empty)
                (Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Knight))) \ "gladiatorType").as[String] should not be (empty)
            }
            "have a non-empty Json representation" in {
                Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Archer))).toString should not be (empty)
                Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Tank))).toString should not be (empty)
                Json.toJson(GladiatorFactory.createGladiator(gladiatorType = Some(GladiatorType.Knight))).toString should not be (empty)
            }
        }
    }
}
