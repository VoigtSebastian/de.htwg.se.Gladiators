package de.htwg.se.gladiators.model;

case class Player(name: String = "NO_NAME", var credits: Int = 50, baseHP: Int = 200, var boughtGladiator: Boolean = false, enemyBaseLine: Int = 0) {
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
}
