package de.htwg.se.gladiators
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model.fileIoComponent.FileIOInterface
import de.htwg.se.gladiators.model.playingFieldComponent._
import de.htwg.se.gladiators.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.gladiators.model.fileIoComponent._

class GladiatorsModule extends AbstractModule with ScalaModule {

    val defaultSize: Int = 15

    override def configure(): Unit = {

        bind[ControllerInterface].to[controllerBaseImpl.Controller]
        bind[PlayingFieldInterface].to[playingFieldBaseImpl.PlayingField]
        bind[FileIOInterface].to[fileIoJsonImpl.FileIO]

        bind[PlayingField].toInstance(new PlayingField(defaultSize))

    }
}

