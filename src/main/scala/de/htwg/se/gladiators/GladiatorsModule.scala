package de.htwg.se.gladiators
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model.playingFieldComponent.PlayingField
import net.codingwell.scalaguice.ScalaModule

class GladiatorsModule extends AbstractModule with ScalaModule {

  val defaultSize: Int = 15
  val playingField: PlayingField = PlayingField()

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[ControllerInterface].to[controllerBaseImpl.Controller]
  }
}
