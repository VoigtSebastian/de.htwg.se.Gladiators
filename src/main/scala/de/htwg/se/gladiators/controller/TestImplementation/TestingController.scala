package de.htwg.se.gladiators.controller.TestImplementation

import de.htwg.se.gladiators.controller.ControllerInterface
import de.htwg.se.gladiators.util.Command
import scala.collection.mutable.Queue
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Events
import de.htwg.se.gladiators.util.Command
import de.htwg.se.gladiators.util.Events._

case class TestingController() extends ControllerInterface {
    val testError = ErrorMessage("These are test functions, the return value is always and Error")
    var commandQueue: Queue[Command] = Queue()

    override def inputCommand(command: Command) = {
        commandQueue.enqueue(command)
        testError
    }
    override def boardToString = "???"
    override def shopToString = "???"

    def namePlayerOne(name: String): Events = {
        commandQueue.enqueue(Command.NamePlayerOne(name))
        testError
    }

    def namePlayerTwo(name: String): Events = {
        commandQueue.enqueue(Command.NamePlayerTwo(name))
        testError
    }

    def endTurn: Events = {
        commandQueue.enqueue(Command.EndTurn)
        testError
    }

    def buyUnit(number: Int, position: Coordinate): Events = {
        commandQueue.enqueue(Command.BuyUnit(number, position))
        testError
    }

    def move(from: Coordinate, to: Coordinate): Events = {
        commandQueue.enqueue(Command.Move(from, to))
        testError
    }
}
