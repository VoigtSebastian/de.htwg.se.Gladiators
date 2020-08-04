package de.htwg.se.gladiators.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.aview.TestImplementation.EventQueue
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Command._

class ControllerSpec extends AnyWordSpec with Matchers {
    "A controller" when {
        "created" should {
            val controller = Controller(15)
            "be in the NamingPlayerOne state" in {
                controller.gameState should be(GameState.NamingPlayerOne)
            }
        }
        "receiving commands in init state" should {
            val controller = Controller(15)
            val eventQueue = EventQueue(controller)
            "send out an init Event" in {
                controller.publish(Init)
                eventQueue.events.dequeue() should be(Init)
            }
            "send out an Error message" in {
                controller.inputCommand(EndTurn)
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }
        }
        "receiving commands to switch through states" should {
            val controller = Controller(15)
            val eventQueue = EventQueue(controller)
            "send out player named events" in {
                controller.inputCommand(NamePlayerOne("helmut"))
                eventQueue.events.dequeue() should be(PlayerOneNamed("helmut"))

                controller.inputCommand(NamePlayerTwo("herman"))
                eventQueue.events.dequeue() should be(PlayerTwoNamed("herman"))
                eventQueue.events.dequeue() should be(Turn(controller.playerOne.get))
            }
            "send out error messages" in {
                controller.inputCommand(NamePlayerOne("helmut"))

                controller.inputCommand(NamePlayerTwo("herman"))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }
            "end the current turn" in {
                controller.inputCommand(EndTurn)
                controller.inputCommand(EndTurn)

                eventQueue.events.dequeue() should be(Turn(controller.playerTwo.get))
                eventQueue.events.dequeue() should be(Turn(controller.playerOne.get))
            }
        }
    }
}
