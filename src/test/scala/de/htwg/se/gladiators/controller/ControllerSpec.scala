package de.htwg.se.gladiators.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.aview.TestImplementation.EventQueue
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.Player

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
        "receiving buy commands" should {
            "send out error messages" in {
                val controller = Controller(15)
                val eventQueue = EventQueue(controller)
                controller.shop = ShopFactory.initRandomShop(5)

                controller.gameState = NamingPlayerOne
                controller.inputCommand(BuyUnit(0))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)

                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(10))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }
            "send out successful messages" in {
                val controller = Controller(15)
                val eventQueue = EventQueue(controller)
                controller.shop = ShopFactory.initRandomShop(5)

                controller.playerOne = Some(Player("", 0, 10000, Vector()))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1))
                eventQueue.events.dequeue().isInstanceOf[SuccessfullyBoughtGladiator] should be(true)

                controller.playerTwo = Some(Player("", 0, 10000, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.inputCommand(BuyUnit(1))
                eventQueue.events.dequeue().isInstanceOf[SuccessfullyBoughtGladiator] should be(true)

                controller.playerOne.get.credits should be >= 0
                controller.playerTwo.get.credits should be >= 0
            }
        }
    }
}
