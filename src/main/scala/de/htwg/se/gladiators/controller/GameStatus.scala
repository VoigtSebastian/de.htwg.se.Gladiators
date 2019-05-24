package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.controller.GameStatus.IDLE

object GameStatus extends Enumeration {

  type GameStatus = Value
   val  P1, P2, END, IDLE = Value

  val map = Map[GameStatus, String](
    P1 -> "Player One",
    P2 -> "Player Two",
    END -> "End of Game",
    IDLE -> ""
  )


  def message(gameStatus: GameStatus): String = {
    map(gameStatus)
  }

}
