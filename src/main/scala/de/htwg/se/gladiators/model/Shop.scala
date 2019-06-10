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
        Gladiator(-1, -1, movementPoints, attackPoints, healthPoints, gladiatorType, Player())
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
            case GladiatorType.TANK => randomNumberInterval(80, 100)
            case GladiatorType.BOW => randomNumberInterval(30, 50)
            case GladiatorType.SWORD => randomNumberInterval(50, 80)
        }
    }

    def genAttackPointsByType(gladiatorType: GladiatorType): Double = {
        gladiatorType match {
            case GladiatorType.TANK => randomNumberInterval(30, 50)
            case GladiatorType.BOW => randomNumberInterval(50, 80)
            case GladiatorType.SWORD => randomNumberInterval(50, 80)
        }
    }

    def randomNumberInterval(min: Double, max: Double): Double = {
        scala.util.Random.nextDouble() * max + min
    }

    def calcCost(gladiator: Gladiator): Int = {
        //TODO: calculateCost
        Int.MaxValue
    }

    def buy(index: Int): Gladiator = {
        val glad = stock(index)
        stock = stock.filter(f => f == glad)
        glad
    }

    override def toString: String = {
        var ret = "Units available in the shop: \n"
        var i = 0
        for (g <- stock) {
            ret = ret + "Unit " + i + " " + GladiatorType.message(g.gladiatorType) +
                ":\n\tAttackPoints\t-> " + g.ap.toInt +
                "\n\tMovementPoints\t-> " + g.movementPoints.toInt +
                "\n\tHealth Points\t-> " + g.hp.toInt +
                "\n"
            i = i + 1
            //TODO: Add costs
        }
        ret
    }
}
