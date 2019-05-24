package de.htwg.se.gladiators.model

case class Player(name: String = "NO_NAME") {
    var gladiators: List[Gladiator] = List()
    override def toString: String = name
}
