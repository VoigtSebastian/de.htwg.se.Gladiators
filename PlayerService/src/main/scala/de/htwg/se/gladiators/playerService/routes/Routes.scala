package de.htwg.se.gladiators.playerService.routes

import akka.http.scaladsl.server.Directives._

object Routes {
    def routes = concat(
        path("hello") {
            get {
                complete("world")
            }
        })
}
