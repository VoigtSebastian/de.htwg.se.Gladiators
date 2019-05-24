package de.htwg.se.gladiators.controller

import de.htwg.se.gladiators.util.Command

class MoveGladiatorCommand(x:Int, y: Int, xDest:Int, yDest: Int, controller: Controller) extends Command {
  var xOld, yOld, xNew, yNew: Int = 0

  override def doStep: Unit = {
    xOld = x
    yOld = y
    xNew = yDest
    yNew = yDest
    controller.playingField.moveGladiator(x, y, xDest, yDest)
  }

  override def undoStep: Unit = controller.moveGladiator(xNew, yNew, xOld, yOld)
  override def redoStep: Unit = controller.moveGladiator(x, y, xDest, yDest)

}