package de.htwg.se.gladiators.playerModule

import com.google.inject.AbstractModule
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface;
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.controllerBaseImplementation.PlayerController;

import net.codingwell.scalaguice.ScalaModule

class PlayerModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
        bind[PlayerControllerInterface].to[PlayerController]
    }
}

