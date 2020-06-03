package de.htwg.se.gladiators.playerModule.model.playerComponent.playerBaseImplementation
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.JsObject

case class Player(name: String = "NO_NAME", var credits: Int = 50, baseHP: Int = 200, var boughtGladiator: Boolean = false, enemyBaseLine: Int = 0) extends PlayerInterface with PlayJsonSupport {
    override def toString: String = name

    override def equals(that: Any): Boolean =
        that match {
        case that: Player => that.canEqual(this) && this.name == that.name && this.enemyBaseLine == that.enemyBaseLine
        case _ => false
    }

    def !=(p: Player): Boolean = {
        p.enemyBaseLine != enemyBaseLine
    }

    def updateName(name: String): Player = {
        this.copy(name = name)
    }

    def addCredits(addedCredits: Int): Player = {
        this.copy(credits = credits + addedCredits)
    }

    def buyItem(costs: Int): Player = {
        credits = credits - costs
        this
    }

    def baseAttacked(ap: Int): Player = {
        this.copy(baseHP = baseHP - ap)
    }

    def updateBoughtGladiator(bought: Boolean): Player = {
        boughtGladiator = bought
        this
    }

    def updateEnemyBaseLine(line: Int) : Player = {
        this.copy(enemyBaseLine = line)
    }

    def toJson: JsObject = Json.obj(
        "name" -> this.name,
        "credits" -> this.credits,
        "baseHP" -> this.baseHP,
        "boughtGladiator" -> this.boughtGladiator,
        "enemyBaseLine" -> this.enemyBaseLine
    )

    def fromJson(jsValue: JsValue): PlayerInterface = {
        val name = (jsValue \ "name").get.as[String]
        val credits = (jsValue \ "credits").get.as[Int]
        val baseHP = (jsValue \ "baseHP").get.as[Int]
        val boughtGladiator = (jsValue \ "boughtGladiator").get.as[Boolean]
        val enemyBaseLine = (jsValue \ "enemyBaseLine").get.as[Int]
        this.copy(name, credits, baseHP, boughtGladiator, enemyBaseLine)
    }
}
