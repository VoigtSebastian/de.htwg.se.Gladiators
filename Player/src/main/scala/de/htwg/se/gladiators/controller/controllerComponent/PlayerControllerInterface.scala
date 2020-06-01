package de.htwg.se.gladiators.playerModule.controller.controllerComponent

import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

trait PlayerControllerInterface {
    def baseAttacked(player: PlayerInterface, ap: Int): PlayerInterface
    def updateName(player: PlayerInterface, name: String): PlayerInterface
}