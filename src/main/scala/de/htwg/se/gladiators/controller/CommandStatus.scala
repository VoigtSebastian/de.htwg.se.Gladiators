package de.htwg.se.gladiators.controller

object CommandStatus extends Enumeration {

  type CommandStatus = Value
   val  CR, MV, AT, IDLE = Value

  val map = Map[CommandStatus, String](
    CR -> "Create",
    MV -> "Move",
    AT -> "Attack",
    IDLE -> "Waiting"
  )

  def message(commandStatus: CommandStatus): String = {
    map(commandStatus)
  }

}
