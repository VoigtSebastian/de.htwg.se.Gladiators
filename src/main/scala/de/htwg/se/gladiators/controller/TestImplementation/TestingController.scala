package de.htwg.se.gladiators.controller.TestImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import scala.collection.mutable.Queue

case class TestingController() extends ControllerInterface {
    var commandQueue: Queue[Command] = Queue()

    override def inputCommand(command: Command) = commandQueue.enqueue(command)

}
