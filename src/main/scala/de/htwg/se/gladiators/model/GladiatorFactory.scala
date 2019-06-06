package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType.GladiatorType

object GladiatorFactory {

  def createGladiator(line: Int, row: Int, gladiatorType: GladiatorType) = {
    var ap : Int = 0
    var hp : Int = 0
    var mp : Int = 0

    gladiatorType match {
      case GladiatorType.SWORD =>
        ap = 50
        hp = 100
        mp = 3
      case GladiatorType.TANK =>
        ap = 30
        hp = 200
        mp = 1
      case GladiatorType.BOW =>
        ap = 50
        hp = 100
        mp = 2
    }

    Gladiator(line, row, mp, ap, hp , gladiatorType)
  }

}
