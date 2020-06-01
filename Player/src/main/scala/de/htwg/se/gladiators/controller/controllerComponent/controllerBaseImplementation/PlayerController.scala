package de.htwg.se.gladiators.playerModule.controller.controllerComponent.controllerBaseImplementation

import com.google.inject.Inject
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface;
import de.htwg.se.gladiators.playerModule.model.playerComponent.PlayerInterface

case class PlayerController @Inject()() extends PlayerControllerInterface {

    def baseAttacked(player: PlayerInterface, ap: Int): PlayerInterface = {
        player.baseAttacked(ap)
    }

    def updateName(player: PlayerInterface, name: String): PlayerInterface = {
        player.updateName(name)
    }
}