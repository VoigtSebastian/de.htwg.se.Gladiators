package de.htwg.se.gladiators.controller.BaseImplementation

import de.htwg.se.gladiators.util.Factories.ShopFactory.initRandomShop
import de.htwg.se.gladiators.util.Factories.BoardFactory.initRandomBoard

object ControllerFactory {
    def newController: Controller = Controller(
        15,
        None,
        None,
        Some(initRandomBoard()),
        initRandomShop())
}