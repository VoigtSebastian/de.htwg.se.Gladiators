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
import de.htwg.se.gladiators.util.Factories.GladiatorFactory
import de.htwg.se.gladiators.model.TileType.{ Mine, Sand }
import de.htwg.se.gladiators.util.Configuration

class ControllerSpec extends AnyWordSpec with Matchers {
    "A controller" when {
        val baseConfig = Configuration(5, 15)
        "created" should {
            val controller = Controller(baseConfig)
            controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))
            controller.playerOne = Some(Player("", 0, 0, 100, false, Vector()))
            "be in the NamingPlayerOne state" in {
                controller.gameState should be(GameState.NamingPlayerOne)
            }
            "have an initialized board" in {
                controller.board.isInstanceOf[Board] should be(true)
            }
            "return the board as a simple string" in {
                controller.boardToString should not be (empty)
            }
            "return the board as a colored string" in {
                controller.boardToColoredString should not be (empty)
            }
            "return the shop as a string" in {
                controller.shopToString should not be (empty)
            }
            "be able to reset Gladiators" in {
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = (0 to 10).map(_ => GladiatorFactory.initRandomGladiator).toVector))
                controller.playerOne = (controller
                    .resetGladiatorsMoved(controller.playerOne.get))

                controller
                    .playerOne
                    .get
                    .gladiators
                    .foreach(_.moved should be(false))
            }
        }
        "returning the current player" should {
            "return Player One" in {
                val controller = Controller(baseConfig)
                controller.playerOne = Some(Player("", 0, 0, 100, false, Vector()))
                controller.gameState = TurnPlayerOne
                controller.currentPlayer should be(controller.playerOne)
            }
            "return Player Two" in {
                val controller = Controller(baseConfig)
                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.currentPlayer should be(controller.playerTwo)
            }
            "return None" in {
                val controller = Controller(baseConfig)
                controller.gameState = NamingPlayerOne
                controller.currentPlayer should be(None)
                controller.gameState = NamingPlayerTwo
                controller.currentPlayer should be(None)
            }
        }
        "returning the enemy player" should {
            "return Player One" in {
                val controller = Controller(baseConfig)
                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))
                controller.gameState = TurnPlayerOne
                controller.enemyPlayer should be(controller.playerTwo)
            }
            "return Player Two" in {
                val controller = Controller(baseConfig)
                controller.playerOne = Some(Player("", 0, 0, 100, false, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.enemyPlayer should be(controller.playerOne)
            }
            "return None" in {
                val controller = Controller(baseConfig)
                controller.gameState = NamingPlayerOne
                controller.enemyPlayer should be(None)
                controller.gameState = NamingPlayerTwo
                controller.enemyPlayer should be(None)
            }
        }
        "updating the current player" should {
            "throw an exception" in {
                val controller = Controller(baseConfig)
                the[Exception] thrownBy controller.updateCurrentPlayer(None) should have message controller.uncheckedStateMessage
            }
            "update player one correctly" in {
                val controller = Controller(baseConfig)
                controller.gameState = GameState.TurnPlayerOne
                controller.playerOne = Some(Player("", 0, 0, 100, false, Vector()))
                controller.updateCurrentPlayer(None)
                controller.playerOne should be(None)
            }
            "update player two correctly" in {
                val controller = Controller(baseConfig)
                controller.gameState = GameState.TurnPlayerTwo
                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))
                controller.updateCurrentPlayer(None)
                controller.playerTwo should be(None)
            }
        }
        "updating the enemy player" should {
            "throw an exception" in {
                val controller = Controller(baseConfig)
                the[Exception] thrownBy controller.updateEnemyPlayer(None) should have message controller.uncheckedStateMessage
            }
            "update player one correctly" in {
                val controller = Controller(baseConfig)
                controller.gameState = GameState.TurnPlayerTwo
                controller.playerOne = Some(Player("", 0, 0, 100, false, Vector()))
                controller.updateEnemyPlayer(None)
                controller.playerOne should be(None)
            }
            "update player two correctly" in {
                val controller = Controller(baseConfig)
                controller.gameState = GameState.TurnPlayerOne
                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))
                controller.updateEnemyPlayer(None)
                controller.playerTwo should be(None)
            }
        }
        "receiving commands in init state" should {
            val controller = Controller(baseConfig)
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
            val controller = Controller(baseConfig)
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
            "fail because the Player already bought a gladiator from the shop this turn" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))
                controller.board = BoardFactory.initRandomBoard(percentageSand = 0)

                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, Int.MaxValue, 100, true, Vector()))
                controller.gameState = TurnPlayerOne
                controller.buyUnit(1, Coordinate((controller.board.tiles.size / 2), 1))
                eventQueue.events.dequeue.isInstanceOf[ErrorMessage] should be(true)
            }
            "fail because all tiles are blocked" in {
                val initialCredits = 1000
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))
                controller.board = BoardFactory.initRandomBoard(percentageSand = 0)

                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, initialCredits, 100, false, Vector()))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), 1)))

                controller.playerTwo = Some(Player("", 0, initialCredits, 100, false, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), controller.board.tiles.size - 2)))

                eventQueue.events.dequeue.isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.dequeue.isInstanceOf[ErrorMessage] should be(true)
            }
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
                controller.board = BoardFactory.initRandomBoard(percentageSand = 100)

                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, 0, 100, false, Vector()))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), 1)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }

            "fail because the coordinate is out of bounds" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerOne
                controller.playerOne = Some(Player("", 1000, controller.board.tiles.size, 100, false, Vector()))

                controller.inputCommand(BuyUnit(1, Coordinate(-1, -1)))
                controller.inputCommand(BuyUnit(1, Coordinate(0, controller.board.tiles.size)))
                controller.inputCommand(BuyUnit(1, Coordinate(controller.board.tiles.size, 0)))

                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
            }

            "send out successful messages" in {
                val initialCredits = 1000
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))
                controller.board = BoardFactory.initRandomBoard(percentageSand = 100)

                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, initialCredits, 100, false, Vector()))
                controller.gameState = TurnPlayerOne
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), 1)))
                eventQueue.events.dequeue().asInstanceOf[SuccessfullyBoughtGladiator].player should be(controller.playerOne.get)
                controller.playerOne.get.credits should be(initialCredits - controller.playerOne.get.gladiators(0).calculateCost)

                controller.playerTwo = Some(Player("", 0, initialCredits, 100, false, Vector()))
                controller.gameState = TurnPlayerTwo
                controller.inputCommand(BuyUnit(1, Coordinate((controller.board.tiles.size / 2), controller.board.tiles.size - 2)))
                eventQueue.events.dequeue().asInstanceOf[SuccessfullyBoughtGladiator].player should be(controller.playerTwo.get)
                controller.playerTwo.get.credits should be(initialCredits - controller.playerTwo.get.gladiators(0).calculateCost)

                controller.playerOne.get.credits should be >= 0
                controller.playerTwo.get.credits should be >= 0
            }
        }
        "moving a unit" should {
            "publish an error because of the wrong gameState" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = NamingPlayerOne
                controller.inputCommand(Move(Coordinate(0, 0), Coordinate(1, 1)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.isEmpty should be(true)
            }
            "publish an error because the move is out of bounds" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerOne
                controller.playerOne = Some(Player("", 0, 0, 100, false, Vector(GladiatorFactory.createGladiator(position = Some(Coordinate(0, 0)), moved = Some(false)))))
                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))

                controller.inputCommand(Move(Coordinate(0, 0), Coordinate(-1, -1)))
                eventQueue.events.dequeue().isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.isEmpty should be(true)
            }
            "publish a successful move for player one" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerOne
                controller.board = BoardFactory.initRandomBoard(15, 100)
                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, 0, 100, false, Vector(
                    GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            moved = Some(false),
                            movementPoints = Some(4)))))

                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector()))

                controller.inputCommand(Move(Coordinate(0, 0), Coordinate(1, 0)))
                eventQueue.events.dequeue().isInstanceOf[Moved] should be(true)
                controller.playerOne.get.gladiators.head.moved should be(true)
                eventQueue.events.isEmpty should be(true)
            }
            "publish a successful move for player two" in {
                val (controller, eventQueue) = createControllerEventQueue(shopStockSize = Some(5))

                controller.gameState = TurnPlayerTwo
                controller.board = BoardFactory.initRandomBoard(15, 100)
                controller.playerOne = Some(Player("", controller.board.tiles.size - 1, 0, 100, false, Vector()))

                controller.playerTwo = Some(Player("", 0, 0, 100, false, Vector(
                    GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            moved = Some(false),
                            movementPoints = Some(4)))))

                controller.inputCommand(Move(Coordinate(0, 0), Coordinate(1, 0)))
                eventQueue.events.dequeue().isInstanceOf[Moved] should be(true)
                controller.playerTwo.get.gladiators.head.moved should be(true)
                eventQueue.events.isEmpty should be(true)
            }
        }
        "used to attack" should {
            "return an error message" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.gameState = Finished
                controller.move(Coordinate(0, 0), Coordinate(0, 1)).isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.dequeue.isInstanceOf[ErrorMessage] should be(true)
                eventQueue.events.isEmpty should be(true)
            }
            "return an attacked message" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")
                controller.board = BoardFactory.createSandBoard3x3
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(position = Some(Coordinate(0, 0)), moved = Some(false), movementPoints = Some(2)))))
                controller.playerTwo = Some(controller
                    .playerTwo
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(position = Some(Coordinate(0, 1))))))
                controller.gameState = TurnPlayerOne
                (1 to 3).foreach(_ => eventQueue.events.dequeue)

                controller.move(Coordinate(0, 0), Coordinate(0, 1)).isInstanceOf[Attacked] should be(true)
                eventQueue.events.dequeue.isInstanceOf[Attacked] should be(true)
                eventQueue.events.isEmpty should be(true)
            }
            "return that there is one unit less" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")
                controller.board = BoardFactory.createSandBoard3x3
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(0, 0)),
                        moved = Some(false),
                        movementPoints = Some(2),
                        attackPoints = Some(9000)))))
                controller.playerTwo = Some(controller
                    .playerTwo
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(0, 1)),
                        healthPoints = Some(1)))))
                controller.gameState = TurnPlayerOne
                (1 to 3).foreach(_ => eventQueue.events.dequeue)

                controller.move(Coordinate(0, 0), Coordinate(0, 1)).isInstanceOf[Attacked] should be(true)
                eventQueue.events.dequeue.isInstanceOf[Attacked] should be(true)
                controller.playerTwo.get.gladiators should be(empty)
                eventQueue.events.isEmpty should be(true)
            }
        }
        "used to do base attacks" should {
            "return a BaseAttacked Event" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.board = BoardFactory.createNormalBoard3x3
                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(1, 1)),
                        moved = Some(false),
                        movementPoints = Some(2),
                        attackPoints = Some(1)))))
                (1 to 3).foreach(_ => eventQueue.events.dequeue)
                controller.gameState = TurnPlayerOne

                val healthBeforeAttack = controller.playerTwo.get.health
                controller.move(Coordinate(1, 1), Coordinate(1, controller.playerOne.get.enemyBaseLine)).isInstanceOf[BaseAttacked] should be(true)
                eventQueue.events.dequeue.isInstanceOf[BaseAttacked] should be(true)
                controller.playerTwo.get.health should be(healthBeforeAttack - controller.playerOne.get.gladiators.head.attackPoints)
                eventQueue.events.isEmpty should be(true)
            }
            "return the Won event" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.board = BoardFactory.createNormalBoard3x3
                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(1, 1)),
                        moved = Some(false),
                        movementPoints = Some(2),
                        attackPoints = Some(100)))))
                (1 to 3).foreach(_ => eventQueue.events.dequeue)
                controller.gameState = TurnPlayerOne
                controller.playerTwo = Some(controller.playerTwo.get.copy(health = 1))

                controller.move(Coordinate(1, 1), Coordinate(1, controller.playerOne.get.enemyBaseLine)).isInstanceOf[Won] should be(true)
                eventQueue.events.dequeue.isInstanceOf[Won] should be(true)
                eventQueue.events.isEmpty should be(true)
            }
        }
        "used to mine a mine" should {
            "not deplete the mine" in {
                val mine = Mine(100)
                val (controller, eventQueue) = createControllerEventQueue()
                controller.board = BoardFactory
                    .createNormalBoard3x3
                    .updateTile(Coordinate(0, 1), mine)

                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")

                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(0, 0)),
                        moved = Some(false),
                        movementPoints = Some(2),
                        attackPoints = Some(100)))))
                (1 to 3).foreach(_ => eventQueue.events.dequeue)

                controller.gameState = TurnPlayerOne

                controller.move(Coordinate(0, 0), Coordinate(0, 1))
                eventQueue.events.dequeue.isInstanceOf[Mined] should be(true)
                controller.board.tiles(1)(0) should be(Mine(100 - mine.goldPerHit))
                eventQueue.events.isEmpty should be(true)
            }
            "deplete the mine" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.board = BoardFactory
                    .createNormalBoard3x3
                    .updateTile(Coordinate(0, 1), Mine(1))

                controller.namePlayerOne("One")
                controller.namePlayerTwo("Two")

                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(
                        position = Some(Coordinate(0, 0)),
                        moved = Some(false),
                        movementPoints = Some(2),
                        attackPoints = Some(100)))))
                (1 to 3).foreach(_ => eventQueue.events.dequeue)

                controller.gameState = TurnPlayerOne

                controller
                    .move(Coordinate(0, 0), Coordinate(0, 1))
                    .asInstanceOf[Mined].amount should be(1)
                eventQueue.events.dequeue.isInstanceOf[Mined] should be(true)
                controller.board.tiles(1)(0) should be(Sand)
                eventQueue.events.isEmpty should be(true)
            }
        }
        "receiving commands" should {
            "publish a Shutdown event" in {
                val (controller, eventQueue) = createControllerEventQueue()
                controller.inputCommand(Quit)
                eventQueue.events.dequeue() == Shutdown
                controller.shouldShutdown.get should be(true)
            }
        }
        "being asked if the current player occupies a tile" should {
            "return false" in {
                val (controller, _) = createControllerEventQueue()
                val length = controller.board.tiles.length - 1
                (0 to length).zip(0 to length).foreach({ case (x, y) => controller.tileOccupiedByCurrentPlayer(Coordinate(x, y)) should be(false) })
            }
            "return true" in {
                val (controller, _) = createControllerEventQueue()
                controller.namePlayerOne("jürgen")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory.createGladiator(position = Some(Coordinate(0, 0))))))
                controller.gameState = TurnPlayerOne
                controller.tileOccupiedByCurrentPlayer(Coordinate(0, 0)) should be(true)
            }
        }
        "being asked for attack tiles from a certain position" should {
            "return false because there is no current Player" in {
                val (controller, _) = createControllerEventQueue()
                val length = controller.board.tiles.length - 1
                (0 to length).zip(0 to length).foreach({ case (x, y) => controller.attackTiles(Coordinate(x, y)) should be(None) })
            }
            "return true" in {
                val (controller, _) = createControllerEventQueue()
                controller.board = BoardFactory.createSandBoard3x3
                controller.namePlayerOne("jürgen")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            movementPoints = Some(1)))))
                controller.gameState = TurnPlayerOne
                controller
                    .attackTiles(Coordinate(0, 0)) should not be empty
            }
            "return None because the tile is not occupied" in {
                val (controller, _) = createControllerEventQueue()
                controller.board = BoardFactory.createSandBoard3x3
                controller.namePlayerOne("jürgen")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            movementPoints = Some(1)))))
                controller.gameState = TurnPlayerOne
                controller
                    .attackTiles(Coordinate(1, 0)) should be(None)
            }
        }
        "being asked for move tiles from a certain position" should {
            "return false because there is no current Player" in {
                val (controller, _) = createControllerEventQueue()
                val length = controller.board.tiles.length - 1
                (0 to length).zip(0 to length).foreach({ case (x, y) => controller.moveTiles(Coordinate(x, y)) should be(None) })
            }
            "return true" in {
                val (controller, _) = createControllerEventQueue()
                controller.board = BoardFactory.createSandBoard3x3
                controller.namePlayerOne("jürgen")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            movementPoints = Some(1)))))
                controller.gameState = TurnPlayerOne
                controller
                    .moveTiles(Coordinate(0, 0)) should not be empty
            }
            "return None because the tile is not occupied" in {
                val (controller, _) = createControllerEventQueue()
                controller.board = BoardFactory.createSandBoard3x3
                controller.namePlayerOne("jürgen")
                controller.playerOne = Some(controller
                    .playerOne
                    .get
                    .copy(gladiators = Vector(GladiatorFactory
                        .createGladiator(
                            position = Some(Coordinate(0, 0)),
                            movementPoints = Some(1)))))
                controller.gameState = TurnPlayerOne
                controller
                    .moveTiles(Coordinate(1, 0)) should be(None)
            }
        }
        "being tiles to place new units" should {
            "return false" in {
                val (controller, _) = createControllerEventQueue()
                controller.newUnitPlacementTiles should be(None)
            }
            "return true" in {
                val (controller, _) = createControllerEventQueue()
                controller.board = BoardFactory.createSandBoard3x3
                controller.namePlayerOne("jürgen")
                controller.gameState = TurnPlayerOne
                controller
                    .newUnitPlacementTiles should not be empty
            }
        }
    }
    "being asked for the TileTypes of the board" should {
        "return them" in {
            val (controller, _) = createControllerEventQueue()
            controller.boardTiles should be(controller.board.tiles)
        }
    }
    "being asked for gladiators" should {
        "return None" in {
            val controller = Controller(Configuration(5, 15))
            controller.gladiatorsPlayerOne should be(None)
            controller.gladiatorsPlayerTwo should be(None)
        }
        "return Some" in {
            val (controller, _) = createControllerEventQueue()
            controller.namePlayerOne("one")
            controller.namePlayerTwo("two")
            controller.playerOne = Some(controller.playerOne.get.copy(gladiators = Vector(GladiatorFactory.initRandomGladiator)))
            controller.playerTwo = Some(controller.playerTwo.get.copy(gladiators = Vector(GladiatorFactory.initRandomGladiator)))

            controller.gladiatorsPlayerOne.get should not be (empty)
            controller.gladiatorsPlayerTwo.get should not be (empty)
        }
    }
    "being asked for the Gladiators in stock" should {
        "return the shops stock" in {
            val controller = Controller(Configuration(5, 15))
            controller.stock should be(controller.shop.stock)
        }
    }
    def createControllerEventQueue(shopStockSize: Option[Int] = None) = {
        val controller = Controller(Configuration(5, 15))
        val eventQueue = EventQueue(controller)
        controller.shop = ShopFactory.initRandomShop(shopStockSize.getOrElse(controller.shop.stock.length))
        (controller, eventQueue)
    }
}
