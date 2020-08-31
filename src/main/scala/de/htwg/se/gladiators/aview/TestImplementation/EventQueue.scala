package de.htwg.se.gladiators.aview.TestImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import scala.swing.Reactor
import scala.collection.mutable.Queue
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Events._

case class EventQueue(controller: ControllerInterface) extends Reactor {
    var currentController = controller
    listenTo(currentController)
    val events: Queue[Events] = Queue()

    reactions += {
        case Init => events.enqueue(Init)
        case PlayerOneNamed(newController, name) => {
            changeController(newController)
            events.enqueue(PlayerOneNamed(newController, name))
        }
        case PlayerTwoNamed(newController, name) => {
            changeController(newController)
            events.enqueue(PlayerTwoNamed(newController, name))
        }
        case Turn(player) => events.enqueue(Turn(player))
        case ErrorMessage(message) => events.enqueue(ErrorMessage(message))
    }

    def changeController(newController: ControllerInterface) = {
        deafTo(currentController)
        currentController = newController
        listenTo(newController)
    }
}
