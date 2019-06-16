package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType.GladiatorType

class Shop(amountGladiatorsInStock: Int) {

    var stock: List[Gladiator] = List()
    for (i <- 0 until amountGladiatorsInStock) {
        stock = stock ::: genGlad() :: Nil
    }

    def genGlad(): Gladiator = {
        val gladiatorType = GladiatorType.random()
        val movementPoints = genMovementPointsByType(gladiatorType)
        val attackPoints = genAttackPointsByType(gladiatorType)
        val healthPoints = genHealthPointsByType(gladiatorType)
        Gladiator(-2, -2, movementPoints, attackPoints, healthPoints, gladiatorType, Player())
    }

    def genMovementPointsByType(gladiatorType: GladiatorType): Double = {
        gladiatorType match {
            case GladiatorType.TANK => randomNumberInterval(2, 3).toInt
            case GladiatorType.BOW => randomNumberInterval(3, 5).toInt
            case GladiatorType.SWORD => randomNumberInterval(3, 4).toInt
        }
    }

    def genHealthPointsByType(gladiatorType: GladiatorType): Double = {
        gladiatorType match {
            case GladiatorType.TANK => randomNumberInterval(80, 100).toInt
            case GladiatorType.BOW => randomNumberInterval(30, 50).toInt
            case GladiatorType.SWORD => randomNumberInterval(50, 80).toInt
        }
    }

    def genAttackPointsByType(gladiatorType: GladiatorType): Double = {
        gladiatorType match {
            case GladiatorType.TANK => randomNumberInterval(30, 50).toInt
            case GladiatorType.BOW => randomNumberInterval(50, 80).toInt
            case GladiatorType.SWORD => randomNumberInterval(50, 80).toInt
        }
    }

    def randomNumberInterval(min: Double, max: Double): Double = {
        scala.util.Random.nextDouble() * max + min
    }

    def calcCost(gladiator: Gladiator): Int = {
        (((gladiator.ap + gladiator.hp) * gladiator.movementPoints) / 50).toInt
    }

    def buy(index: Int, player: Player): Option[Gladiator] = {
        if (index >= stock.size || index < 0)
            return None
        val glad = stock(index)
        val cost = calcCost(glad)
        if (cost > player.credits)
            return None

        player.buyItem(cost)
        stock = stock.filter(f => f != glad)
        stock = stock ::: genGlad() :: Nil
        Option(glad)
    }

    override def toString: String = {
        var ret = "Units available in the shop: \n"
        var i = 0
        for (g <- stock) {
            ret = ret + "Unit " + i + " " + GladiatorType.message(g.gladiatorType) +
                ":\n\tAttackPoints\t-> " + g.ap.toInt +
                "\n\tMovementPoints\t-> " + g.movementPoints.toInt +
                "\n\tHealth Points\t-> " + g.hp.toInt +
                "\n\tCost\t\t\t-> " + calcCost(g) +
                "\n"
            i = i + 1
        }
        ret
    }
}
