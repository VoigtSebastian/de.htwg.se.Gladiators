package de.htwg.se.gladiators.playerModule

import com.google.inject.{Guice, Injector}
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.controllerBaseImplementation.PlayerController
 

object PlayerMod {
  val injector: Injector = Guice.createInjector(new PlayerModule)
  val controller: PlayerControllerInterface = injector.getInstance(classOf[PlayerController])
  @volatile var exitServer = false

  val httpServer: PlayerHttpServer = PlayerHttpServer(controller)

  def main(args: Array[String]): Unit = {
    
    while (!exitServer) {
      Thread.sleep(1000)
    }
    httpServer.shutdownWebServer()
    
  }
}