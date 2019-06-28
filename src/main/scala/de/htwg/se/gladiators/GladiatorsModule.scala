package de.htwg.se.gladiators
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model.playingFieldComponent._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import net.codingwell.scalaguice.ScalaModule

class GladiatorsModule extends AbstractModule with ScalaModule {

  val defaultSize: Int = 15

  def configure(): Unit = {

    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[ControllerInterface].to[controllerBaseImpl.Controller]
    bind[PlayingFieldInterface].to[playingFieldBaseImpl.PlayingField]
    bind[PlayingField].toInstance(new PlayingField(defaultSize))
  }
}
