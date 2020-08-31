package de.htwg.se.gladiators.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.aview.TestImplementation.EventQueue
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.controller.GameState.TurnPlayerOne
import de.htwg.se.gladiators.controller.GameState.NamingPlayerOne
import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.controller.BaseImplementation.ControllerFactory.newController

class ControllerSpec extends AnyWordSpec with Matchers {
    "A controller" when {
        "created" should {
            val controller = newController
            "be in the NamingPlayerOne state" in {
                controller.gameState should be(GameState.NamingPlayerOne)
            }
        }
        "receiving commands in init state" should {
            val controller = newController
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
            "send out player named events" in {
                val controller = newController
                val eventQueue = EventQueue(controller)

                controller.inputCommand(NamePlayerOne("helmut"))
                // Will fail if the command did not work
                val updatedController = eventQueue.events.dequeue.asInstanceOf[PlayerOneNamed]
                
                updatedController.newController.inputCommand(NamePlayerTwo("herman"))
                eventQueue.events.dequeue.isInstanceOf[PlayerTwoNamed] should be (true)
                eventQueue.events.dequeue.isInstanceOf[Turn] should be (true)
            }
            "send out error messages" in {
                val controller = newController
                val eventQueue = EventQueue(controller)

                controller.inputCommand(NamePlayerOne("helmut"))
                // Will fail if the command did not work
                eventQueue
                    .events
                    .dequeue
                    .asInstanceOf[PlayerOneNamed]
                    .newController
                    .inputCommand(NamePlayerOne("herman"))

                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }
            "end the current turn" in {
                val controller = newController
                    .copy(playerOne = Some(Player("", 0, 0, Vector())),
                          playerTwo = Some(Player("", 0, 0, Vector())))
                controller.gameState = TurnPlayerOne
                val eventQueue = EventQueue(controller)

                controller.inputCommand(EndTurn)
                controller.inputCommand(EndTurn)

                eventQueue.events.dequeue() should be(Turn(controller.playerTwo.get))
                eventQueue.events.dequeue() should be(Turn(controller.playerOne.get))
            }
        }
        "receiving buy commands" should {
            "send out error messages" in {
                val controller = newController.copy(shop = ShopFactory.initRandomShop(5))
                val eventQueue = EventQueue(controller)

                controller.gameState = NamingPlayerOne
                controller.inputCommand(BuyUnit(0))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)

                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(10))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }
            "send out successful messages" in {
                val controller = newController.copy(playerOne = Some(Player("", 0, 0, Vector())))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1))
            }
        }
    }
}
