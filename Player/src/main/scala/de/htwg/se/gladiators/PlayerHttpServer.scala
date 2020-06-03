package de.htwg.se.gladiators.playerModule

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse, ContentTypes, MediaTypes }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface;
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.htwg.se.gladiators.playerModule.util._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scala.util.Properties.envOrElse

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{ JsValue, Json }
import de.htwg.se.gladiators.playerModule.model.playerComponent.playerBaseImplementation.Player

case class PlayerHttpServer(controller: PlayerControllerInterface) extends PlayJsonSupport {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = concat(
        get {
            path("gladiators" / "player" / "static") {
                complete(Json.obj("name" -> "gladiators"))
            }
        },
        put {
            path("gladiators" / "player" / "updateName") {
                entity(as[UpdateNameArgumentContainer]) { params =>
                    println(params.player)
                    println(params.name)
                    var player = params.player.updateName(params.name)
                    complete(player.toJson)
                }
            }
        },
        put {
            path("gladiators" / "player" / "baseAttacked") {
                entity(as[BaseAttackedArgumentContainer]) { params =>
                    val player = controller.baseAttacked(params.player, params.ap).toJson
                    complete(player)
                }
            }
        })

    val port = envOrElse("PLAYER-SERVICE-PORT", "8081").toInt
    val domain = envOrElse("DOMAIN", "localhost")

    val bindingFuture = Http().bindAndHandle(route, domain, port)
    println(s"Player Rest Service started on $domain $port")

    def shutdownWebServer(): Unit = {
        bindingFuture
            .flatMap(_.unbind()) // trigger unbinding from the port
            .onComplete(_ => system.terminate()) // and shutdown when done
    }
}
