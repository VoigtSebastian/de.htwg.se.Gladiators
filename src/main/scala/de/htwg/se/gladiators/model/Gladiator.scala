package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GaldiatorType.GladiatorType


case class Gladiator(x: Int, y: Int, movementPoints: Double, ap: Double, hp: Double, gladiatortype: GladiatorType) {

  def levelUp(value: Int): Boolean = {
    this.ap += value;
    return true;
  }
}
