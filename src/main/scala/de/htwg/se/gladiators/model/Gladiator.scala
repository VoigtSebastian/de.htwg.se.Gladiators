package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType._


case class Gladiator(var line: Int, var row: Int, movementPoints: Double, var ap: Double, hp: Double, gladiatorType: GladiatorType) {

  def levelUp(value: Int): Unit = {
    this.ap += value
  }
  def move(line: Int, row: Int): Unit = {
    this.line = line
    this.row = row
  }
}
