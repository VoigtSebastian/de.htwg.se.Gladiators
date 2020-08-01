package de.htwg.se.gladiators.model

case class Player(
    name: String,
    gladiators: Vector[Gladiator] = Vector())
