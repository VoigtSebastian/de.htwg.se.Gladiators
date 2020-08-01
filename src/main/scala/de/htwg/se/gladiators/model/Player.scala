package de.htwg.se.gladiators.model

case class Player(
    name: String,
    enemyBaseLine: Int,
    gladiators: Vector[Gladiator] = Vector())
