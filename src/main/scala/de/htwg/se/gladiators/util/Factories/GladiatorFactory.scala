package de.htwg.se.gladiators.util.Factories

import de.htwg.se.gladiators.model.Gladiator
import de.htwg.se.gladiators.model.GladiatorType
import scala.util.Random
import de.htwg.se.gladiators.model.GladiatorType._
import de.htwg.se.gladiators.util.Coordinate

object GladiatorFactory {
    def createGladiator(
        gladiatorType: Option[GladiatorType] = None,
        position: Option[Coordinate] = None,
        healthPoints: Option[Int] = None,
        movementPoints: Option[Int] = None,
        attackPoints: Option[Int] = None,
        moved: Option[Boolean] = None) = {
        val gladiator = initRandomGladiator
        Gladiator(
            gladiatorType.getOrElse(gladiator.gladiatorType),
            position.getOrElse(gladiator.position),
            healthPoints.getOrElse(gladiator.healthPoints),
            movementPoints.getOrElse(gladiator.movementPoints),
            attackPoints.getOrElse(gladiator.attackPoints),
            moved.getOrElse(true))
    }

    // def curryGladiator(gladiatorType: GladiatorType)(position: Coordinate)(healthPoints: Int)(movementPoints: Int)(attackPoints: Int) = Gladiator(gladiatorType, position, healthPoints, movementPoints, attackPoints)

    def initRandomGladiator: Gladiator = createType match {
        case Archer => Gladiator(
            Archer,
            Coordinate(-1, -1),
            randomNumberInterval(40, 70).toInt,
            randomNumberInterval(1, 3).toInt,
            randomNumberInterval(40, 70).toInt,
            true)

        case Knight => Gladiator(
            Knight,
            Coordinate(-1, -1),
            randomNumberInterval(40, 70).toInt,
            randomNumberInterval(2, 3).toInt,
            randomNumberInterval(40, 70).toInt,
            true)

        case Tank => Gladiator(
            Tank,
            Coordinate(-1, -1),
            randomNumberInterval(40, 70).toInt,
            randomNumberInterval(1, 2).toInt,
            randomNumberInterval(40, 70).toInt,
            true)
    }

    def createType: GladiatorType = GladiatorType.values(Random.nextInt(GladiatorType.values.length))

    def randomNumberInterval(min: Int, max: Int): Int = {
        val range = max - min
        val rand = scala.util.Random.nextInt(range + 1)
        (rand + min).intValue
    }
}
