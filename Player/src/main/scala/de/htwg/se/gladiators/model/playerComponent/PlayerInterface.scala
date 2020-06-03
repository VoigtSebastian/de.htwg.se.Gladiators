package de.htwg.se.gladiators.playerModule.model.playerComponent

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import de.htwg.se.gladiators.playerModule.model.playerComponent.playerBaseImplementation.Player
import play.api.libs.json._

import scala.xml.{Elem, Node}


trait PlayerInterface {
//    case class Player(name: String = "NO_NAME", var credits: Int = 50, baseHP: Int = 200, var boughtGladiator: Boolean = false, enemyBaseLine: Int = 0) extends PlayerInterface with PlayJsonSupport {
    val name: String
    var credits : Int
    val baseHP: Int
    var boughtGladiator: Boolean
    val enemyBaseLine: Int

    def updateName(name: String): PlayerInterface
    def addCredits(addedCredits: Int): PlayerInterface
    def buyItem(costs: Int): PlayerInterface
    def baseAttacked(ap: Int): PlayerInterface
    def updateBoughtGladiator(bought: Boolean): PlayerInterface
    def updateEnemyBaseLine(line: Int) : PlayerInterface
    def toJson: JsValue
    def fromJson(jsValue: JsValue): PlayerInterface
}

object PlayerInterface extends PlayJsonSupport {
  
    import play.api.libs.json._

    implicit val playerWrites: Writes[PlayerInterface] = {
        case player: PlayerInterface => player.toJson
    }

    implicit val playerReads: Reads[PlayerInterface] = (json: JsValue) => {
        val player = Player().fromJson(json)
        JsSuccess(player)
    }
}