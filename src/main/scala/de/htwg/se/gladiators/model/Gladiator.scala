package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType._


case class Gladiator(cell: Cell, movementPoints: Double, var ap: Double, hp: Double, gladiatortype:  GladiatorType) {

  def levelUp(value: Int): Unit = {
    this.ap += value
  }
}
