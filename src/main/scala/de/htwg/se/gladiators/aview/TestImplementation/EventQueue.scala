package de.htwg.se.gladiators.aview.TestImplementation

import scala.collection.mutable.Queue
import scala.swing.Reactor

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Events

case class EventQueue(controller: ControllerInterface) extends Reactor {
    listenTo(controller)
    val events: Queue[Events] = Queue()

    reactions += { case e: Events => events.enqueue(e) }
}
