package de.htwg.se.gladiators.playerModule

import com.google.inject.{ Guice, Injector }

object PlayerMod {
    @volatile var exitServer = false

    val httpServer: PlayerHttpServer = PlayerHttpServer()

    def main(args: Array[String]): Unit = {

        while (!exitServer) {
            Thread.sleep(1000)
        }
        httpServer.shutdownWebServer()

    }
}
