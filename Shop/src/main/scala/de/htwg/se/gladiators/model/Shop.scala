package de.htwg.se.gladiators.model
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType

case class Shop(amountGladiatorsInStock: Int) {

    var stock: List[(Gladiator, Int)] = List()
    for (i <- 0 until amountGladiatorsInStock) {
        stock = stock ::: (genGlad(), 0) :: Nil
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
            case GladiatorType.TANK => randomNumberInterval(1, 1).toInt
            case GladiatorType.BOW => randomNumberInterval(2, 3).toInt
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
            case GladiatorType.BOW => randomNumberInterval(40, 70).toInt
            case GladiatorType.SWORD => randomNumberInterval(50, 80).toInt
        }
    }

    def randomNumberInterval(min: Int, max: Int): Double = {
        //scala.util.Random.nextDouble() * (max - 1) + min
        val range = max - min
        val rand = scala.util.Random.nextInt(range + 1)
        rand + min
    }

    def calcCost(gladiator: Gladiator): Int = {
        (((gladiator.ap + gladiator.hp) * (gladiator.movementPoints + 1)) / 35).toInt
    }

    def buy(index: Int, player: Player): Option[Gladiator] = {
        if (index >= stock.size || index < 0)
            return None
        var glad = stock(index)._1
        val cost = calcCost(glad)
        if (cost > player.credits)
            return None

        player.buyItem(cost)
        stock = stock.filter(f => f != stock(index))
        stock = stock ::: (genGlad(), 0) :: Nil
        glad = glad.assignPlayer(player)
        player.boughtGladiator = true
        Option(glad)
    }

    def endTurn(): Unit = {
        for (i <- stock.indices)
            stock = stock.updated(i, (stock(i)._1, stock(i)._2 + 1))
    }

    def kickOut(turns: Int): Unit = {
        stock = stock.filter(g => g._2 < turns)
        for (i <- stock.size to amountGladiatorsInStock - 1)
            stock = stock ::: (genGlad(), 0) :: Nil
    }

    override def toString: String = {
        var ret = "Units available in the shop: \n"
        var i = 0
        for (g <- stock) {
            ret = ret + "Unit " + i + " " + GladiatorType.message(g._1.gladiatorType) +
                ":\n\tAttackPoints\t-> " + g._1.ap.toInt +
                "\n\tMovementPoints\t-> " + g._1.movementPoints.toInt +
                "\n\tHealth Points\t-> " + g._1.hp.toInt +
                "\n\tCost\t\t\t-> " + calcCost(g._1) +
                "\n"
            i = i + 1
        }
        ret
    }
}
