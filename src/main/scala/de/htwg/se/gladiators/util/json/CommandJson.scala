package de.htwg.se.gladiators.util.json

import scala.util.Try

import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Coordinate

import play.api.libs.functional.syntax._
import play.api.libs.json._

object CommandJson {
    implicit val coordinateReads: Reads[Coordinate] = (
        (JsPath \ "x").read[Int] and
        (JsPath \ "y").read[Int])(Coordinate.apply _)

    def readCommand(json: JsValue): Try[Command] = Try((json \ "commandType").as[String] match {
        case "Move" => Move((json \ "from").as[Coordinate], (json \ "to").as[Coordinate])
        case "BuyUnit" => BuyUnit((json \ "number").as[Int], (json \ "position").as[Coordinate])
        case "NamePlayerOne" => NamePlayerOne((json \ "name").as[String])
        case "NamePlayerTwo" => NamePlayerTwo((json \ "name").as[String])
        case "EndTurn" => EndTurn
        case "Quit" => Quit
        case _ => throw new Exception("Unsupported command")
    })
}
