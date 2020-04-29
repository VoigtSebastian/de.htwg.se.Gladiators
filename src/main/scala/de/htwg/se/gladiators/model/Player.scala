package de.htwg.se.gladiators.model

case class Player(var name: String = "NO_NAME", var boughtGladiator: Boolean = false, val enemyBaseLine: Int = 0) {
    override def toString: String = name

    var credits: Integer = 50
    var baseHP: Integer = 200

    def buyItem(costs: Int): Player = {
        this.credits -= costs
        this
    }
}
