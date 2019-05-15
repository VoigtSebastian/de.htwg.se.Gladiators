package de.htwg.se.gladiators.controller

object GameStatus extends Enumeration {

  type GameStatus = Value
   val IDLE, P1, P2, END = Value

  val map = Map[GameStatus, String](
    IDLE -> "",
    P1 -> "Player One",
    P2 -> "Player Two",
    END -> "End of Game"
  )

  def message(gameStatus: GameStatus): String = {
    map(gameStatus)
  }

}
