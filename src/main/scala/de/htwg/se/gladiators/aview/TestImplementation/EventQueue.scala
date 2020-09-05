package de.htwg.se.gladiators.aview.TestImplementation

import scala.swing.Reactor
import scala.collection.mutable.Queue
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.controller.ControllerInterface

case class EventQueue(controller: ControllerInterface) extends Reactor {
    listenTo(controller)
    val events: Queue[Events] = Queue()

    reactions += {
        case Init => events.enqueue(Init)
        case PlayerOneNamed(name) => events.enqueue(PlayerOneNamed(name))
        case PlayerTwoNamed(name) => events.enqueue(PlayerTwoNamed(name))
        case Turn(player) => events.enqueue(Turn(player))
        case ErrorMessage(message) => events.enqueue(ErrorMessage(message))
        case SuccessfullyBoughtGladiator(player, gladiator) => events.enqueue(SuccessfullyBoughtGladiator(player, gladiator))
    }
}
