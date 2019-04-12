package de.htwg.se.gladiators.model

case class Player(name: String = "NO_NAME") {
  override def toString:String = name
}
