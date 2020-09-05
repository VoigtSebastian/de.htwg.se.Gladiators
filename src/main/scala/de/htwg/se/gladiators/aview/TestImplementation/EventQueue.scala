package de.htwg.se.gladiators.aview.TestImplementation

import scala.swing.Reactor
import scala.collection.mutable.Queue
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.controller.ControllerInterface

case class EventQueue(controller: ControllerInterface) extends Reactor {
    listenTo(controller)
    val events: Queue[Events] = Queue()

    reactions += { case e: Events => events.enqueue(e) }
}
