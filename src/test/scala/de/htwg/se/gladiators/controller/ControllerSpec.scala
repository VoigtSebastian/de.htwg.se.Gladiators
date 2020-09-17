package de.htwg.se.gladiators.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.gladiators.controller.BaseImplementation.Controller
import de.htwg.se.gladiators.aview.TestImplementation.EventQueue
import de.htwg.se.gladiators.util.Events._
import de.htwg.se.gladiators.util.Command._
import de.htwg.se.gladiators.util.Factories.ShopFactory
import de.htwg.se.gladiators.controller.GameState._
import de.htwg.se.gladiators.model.{ Player, Board }
import de.htwg.se.gladiators.util.Coordinate
import de.htwg.se.gladiators.util.Factories.BoardFactory

class ControllerSpec extends AnyWordSpec with Matchers {
    "A controller" when {
        "created" should {
            val controller = Controller()
            "be in the NamingPlayerOne state" in {
                controller.gameState should be(GameState.NamingPlayerOne)
            }
            "have an initialized board" in {
                controller.board.isInstanceOf[Board] should be(true)
            }
            "return nice string values" in {
                controller.playerTwo = Some(Player("", 0, 0, Vector()))
                controller.playerOne = Some(Player("", 0, 0, Vector()))

                controller.boardToString should not be (empty)
            }
        }
        "returning the current player" should {
            "return Player One" in {
                val controller = Controller()
                controller.playerOne = Some(Player("", 0, 0, Vector()))
                controller.gameState = TurnPlayerOne
                controller.currentPlayer should be(controller.playerOne)
            }
            "return Player Two" in {
                val controller = Controller()
                controller.playerTwo = Some(Player("", 0, 0, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.currentPlayer should be(controller.playerTwo)
            }
            "return None" in {
                val controller = Controller()
                controller.gameState = NamingPlayerOne
                controller.currentPlayer should be(None)
                controller.gameState = NamingPlayerTwo
                controller.currentPlayer should be(None)
            }
        }
        "returning the enemy player" should {
            "return Player One" in {
                val controller = Controller()
                controller.playerTwo = Some(Player("", 0, 0, Vector()))
                controller.gameState = TurnPlayerOne
                controller.enemyPlayer should be(controller.playerTwo)
            }
            "return Player Two" in {
                val controller = Controller()
                controller.playerOne = Some(Player("", 0, 0, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.enemyPlayer should be(controller.playerOne)
            }
            "return None" in {
                val controller = Controller()
                controller.gameState = NamingPlayerOne
                controller.enemyPlayer should be(None)
                controller.gameState = NamingPlayerTwo
                controller.enemyPlayer should be(None)
            }
        }
        "receiving commands in init state" should {
            val controller = Controller()
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
            val controller = Controller()
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
            "fail because of the wrong controller-state" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = NamingPlayerOne
                controller.inputCommand(BuyUnit(1, Coordinate(0, 0)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }

            "fail because the requested Unit does not exist" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(10, Coordinate(0, 0)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }

            "fail because of insufficient credits" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerOne
                controller.playerOne = Some(Player("", 0, controller.board.tiles.size, Vector()))
                controller.inputCommand(BuyUnit(1, Coordinate(0, (controller.board.tiles.size / 2) - 1)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }

            "send out successful messages" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))
                controller.board = BoardFactory.initRandomBoard(percentageSand = 100)

                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, 10000, Vector()))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), 1)))
                eventQueue.events.dequeue().asInstanceOf[SuccessfullyBoughtGladiator].player should be(controller.playerOne.get)

                controller.playerTwo = Some(Player("", 0, 10000, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), controller.board.tiles.size - 2)))
                eventQueue.events.dequeue().asInstanceOf[SuccessfullyBoughtGladiator].player should be(controller.playerTwo.get)

                controller.playerOne.get.credits should be >= 0
                controller.playerTwo.get.credits should be >= 0
            }
        }
    }
    def createControllerEventQueue(shopStockSize: Option[Int]) = {
        val controller = Controller()
        val eventQueue = EventQueue(controller)
        controller.shop = ShopFactory.initRandomShop(shopStockSize.getOrElse(controller.shop.stock.length))
        (controller, eventQueue)
    }
}
