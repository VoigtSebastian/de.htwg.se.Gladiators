package de.htwg.se.gladiators.playerModule

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface;
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.htwg.se.gladiators.playerModule.util._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scala.util.Properties.envOrElse

case class PlayerHttpServer(controller: PlayerControllerInterface) extends PlayJsonSupport {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = concat(
        /*
        get {

            path("gladiators" / "player") {
                complete(controller.baseAttacked)
            }
            *
        },
        */
        put {
            /*
            path("gladiators" / "player") {
                complete(controller.player)
            } ~
            */
            path("gladiators" / "player" / "baseAttacked") {
                entity(as[BaseAttackedArgumentContainer]) { params =>
                    complete(controller.baseAttacked(params.player, params.ap))
                }
            }

            path("gladiators" / "player" / "updateName") {
                entity(as[UpdateNameArgumentContainer]) { params =>
                    complete(controller.updateName(params.player, params.name))
                }
            }
/*
            path("gladiators" / Segment) {
                command => {
                    processInputLine(command)
                    val response = "Command: " + command + "</br>" + "Field:</br>" + playingFieldToHtml
                    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, response))
                }
            }
            */
        }
    )
    val port = envOrElse("SERVICE-PORT", "8081").toInt
    val bindingFuture = Http().bindAndHandle(route, "localhost", port)

    println(s"Player Rest Service started on port $port")

    def shutdownWebServer() : Unit = {
        bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }
}
