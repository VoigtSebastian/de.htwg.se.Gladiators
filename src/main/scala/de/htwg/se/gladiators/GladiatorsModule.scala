package de.htwg.se.gladiators
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model.playingFieldComponent._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import net.codingwell.scalaguice.ScalaModule

class GladiatorsModule extends AbstractModule with ScalaModule {

  def configure(): Unit = {

    bind[ControllerInterface].to[controllerBaseImpl.Controller]

  }
}
