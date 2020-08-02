package de.htwg.se.gladiators.model

case class Player(
    name: String,
    enemyBaseLine: Int,
    credits: Int,
    gladiators: Vector[Gladiator] = Vector())
