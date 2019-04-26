package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType._


case class Gladiator(line: Int, row: Int, movementPoints: Double, var ap: Double, hp: Double, gladiatorType: GladiatorType) {

  def levelUp(value: Int): Unit = {
    this.ap += value
  }
}
