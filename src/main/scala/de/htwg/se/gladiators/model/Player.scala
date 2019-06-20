package de.htwg.se.gladiators.model

case class Player(var name: String = "NO_NAME") {
  override def toString:String = name
  var credits: Integer = 100
  var baseHP: Integer = 200

  def buyItem(costs: Int) : Player = {
    this.credits -= costs
    this
  }
}
