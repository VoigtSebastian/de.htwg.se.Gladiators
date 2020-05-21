package de.htwg.se.gladiators.controller.controllerComponent

import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.util.Command

class MoveGladiatorCommand(x: Int, y: Int, xDest: Int, yDest: Int, controller: Controller) extends Command {
    var xOld, yOld, xNew, yNew: Int = 0

    override def doStep: Unit = {
        xOld = x
        yOld = y
        xNew = xDest
        yNew = yDest
        controller.playingField = controller.playingField.moveGladiator(x, y, xDest, yDest)
    }

    override def undoStep: Unit = {
        val gladiator = controller.getGladiator(xNew, yNew).updateMoved(false)
        controller.playingField = controller.playingField.setGladiator(xNew, yNew, gladiator)
        controller.moveGladiator(xNew, yNew, xOld, yOld)
    }

    override def redoStep: Unit = {
        controller.playingField = controller.playingField.moveGladiator(x, y, xDest, yDest)
    }

}
