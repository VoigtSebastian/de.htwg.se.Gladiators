package de.htwg.se.gladiators.aview

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import de.htwg.se.gladiators.controller.controllerComponent.{ ControllerInterface, GladChanged, PlayingFieldChanged }
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

class HttpServer(controller: ControllerInterface) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = concat(
        get {
            path("gladiators") {
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<p> GLADIATORS </p>"))
            } ~
                path("gladiators" / "playingfield") {
                    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, playingFieldToHtml))
                }
        },
        put {
            path("gladiators" / Segment) {
                command =>
                    {
                        processInputLine(command)
                        val response = "Command: " + command + "</br>" + "Field:</br>" + playingFieldToHtml
                        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, response))
                    }
            }
        })
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    def playingFieldToHtml: String = {
        controller.playingFieldToHtml
    }

    def unbind(): Unit = {
        bindingFuture
            .flatMap(_.unbind())
            .onComplete(_ => system.terminate())
    }

    def processInputLine(command: String): Unit = {
        // validate input here and call controller (similar to TUI)
        controller.nextPlayer()
    }
}