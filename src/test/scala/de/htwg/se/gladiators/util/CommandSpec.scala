package de.htwg.se.gladiators.util

import scala.util.Success

import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.json.CommandJson.readCommand

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._

class CommandSpec extends AnyWordSpec with Matchers {

    val jsonMove: JsValue = Json.parse("""
    {
        "type" : "Move",
        "from": {"x" : 0, "y": 0},
        "to": {"x" : 0, "y": 0}
    }
    """)

    val jsonBuy: JsValue = Json.parse("""
    {
        "type" : "BuyUnit",
        "number": 0,
        "position": {"x" : 0, "y": 0}
    }
    """)

    val jsonNamePlayerOne: JsValue = Json.parse("""
    {
        "type" : "NamePlayerOne",
        "name": "one"
    }
    """)

    val jsonNamePlayerTwo: JsValue = Json.parse("""
    {
        "type" : "NamePlayerTwo",
        "name": "two"
    }
    """)

    val jsonEndTurn: JsValue = Json.parse("""
    {
        "type" : "EndTurn"
    }
    """)

    val jsonQuit: JsValue = Json.parse("""
    {
        "type" : "Quit"
    }
    """)

    "Commands" when {
        "represented in Json" should {
            "be parsed to a Move Command" in {
                readCommand(jsonMove) should be(Success(Move(Coordinate(0, 0), Coordinate(0, 0))))
            }
            "be parsed to a BuyUnit Command" in {
                readCommand(jsonBuy) should be(Success(BuyUnit(0, Coordinate(0, 0))))
            }
            "be parsed to a NamePlayerOne Command" in {
                readCommand(jsonNamePlayerOne) should be(Success(NamePlayerOne("one")))
            }
            "be parsed to a NamePlayerTwo Command" in {
                readCommand(jsonNamePlayerTwo) should be(Success(NamePlayerTwo("two")))
            }
            "be parsed to a EndTurn Command" in {
                readCommand(jsonEndTurn) should be(Success(EndTurn))
            }
            "be parsed to a Quit Command" in {
                readCommand(jsonQuit) should be(Success(Quit))
            }
            "throw an error" in {
                readCommand(Json.parse("""
                {"test": "test"}
                """)).isFailure should be(true)
            }
        }
    }
}
