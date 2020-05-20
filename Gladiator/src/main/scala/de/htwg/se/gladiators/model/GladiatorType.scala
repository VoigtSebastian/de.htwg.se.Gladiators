package de.htwg.se.gladiators.model

object GladiatorType extends Enumeration {
    type GladiatorType = Value
    val SWORD, BOW, TANK = Value

    def random(): GladiatorType = {
        scala.util.Random.nextInt(3) match {
            case 0 => SWORD
            case 1 => BOW
            case _ => TANK
        }
    }

    val map = Map[GladiatorType, String](
        SWORD   -> "Sword",
        BOW     -> "Bow",
        TANK    -> "Tank"
    )


    def message(gladiatorType: GladiatorType): String = {
        map(gladiatorType)
    }
}
